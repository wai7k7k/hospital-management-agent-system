from typing import TypedDict, Annotated, List, Optional
from langgraph.graph import StateGraph, END
from langchain_core.messages import HumanMessage
from app.core.models.request import DiagnosisState, ChatMessage, IntentType
from app.utils.logger import log
import asyncio


class GraphState(TypedDict):
    """LangGraph 状态定义"""
    session_id: str   #会话ID
    messages: List[ChatMessage]   #消息列表
    intent: Optional[IntentType]   #意图
    confidence: Optional[float]   #意图置信度
    symptoms: Optional[str]   #症状描述
    patient_id: Optional[str]  #患者ID
    user_id: Optional[str]  #用户ID
    triage_result: Optional[dict]  #分诊结果
    register_result: Optional[dict]
    report_result: Optional[dict]
    medication_result: Optional[dict]
    error: Optional[str]  #错误信息
    response: Optional[str]  #响应
    metadata: dict   #元信息
    #事件驱动
    event_type: Optional[str]
    event_data: Optional[dict]


async def intent_recognition_node(state: GraphState) -> GraphState:
    """意图识别 — 活跃业务状态优先，避免“确认”等短消息被 LLM 错误改道。"""
    log.info(f"执行意图识别节点 - session_id: {state['session_id']}")

    try:
        from app.core.services.intent_service import IntentService
        intent_service = IntentService()

        last_message = state['messages'][-1].content if state['messages'] else ""
        history = state['messages'][:-1] if len(state['messages']) > 1 else []

        # 确定性的挂号状态必须先于概率性的 LLM 意图识别。
        # 只有患者明确提出另一项业务时才退出挂号流程。
        has_active = await _check_active_register(state['session_id'])
        explicit_switch = any(keyword in last_message for keyword in (
            "报告解读", "分析报告", "检查报告", "用药咨询", "药怎么吃",
            "重新分诊", "智能分诊", "取消挂号流程",
        ))
        if has_active and not explicit_switch:
            state['intent'] = IntentType.REGISTER
            state['confidence'] = 1.0
            state['metadata']['register_session_active'] = True
            return state
        if has_active and explicit_switch:
            await _clear_register_state(state['session_id'])

        result = await intent_service.recognize_intent(last_message, history)
        llm_intent = result['intent']
        llm_confidence = result['confidence']

        # 挂号会话中，LLM 判定用户想干别的事 → 清空挂号状态，走 LLM 意图
        # 无挂号会话 → 纯 LLM 意图
        state['intent'] = llm_intent
        state['confidence'] = llm_confidence
        state['metadata'].update(result.get('metadata', {}))

        log.info(f"意图识别完成 - intent: {state['intent']}, confidence: {state['confidence']}")

    except Exception as e:
        log.error(f"意图识别失败: {str(e)}")
        state['error'] = f"意图识别失败: {str(e)}"
        state['intent'] = IntentType.CHAT

    return state


async def triage_node(state: GraphState) -> GraphState:
    """分诊节点 — 三明治模式：TriageAgent 记忆管理 + ReAct 多轮推理"""
    log.info(f"执行 ReAct 分诊节点 - session_id: {state['session_id']}")

    try:
        from app.core.agents import TriageAgent
        from app.core.graph.react_agent import build_react_agent, parse_json_result
        from app.core.tools.medical_tools import (
            extract_symptoms,
            search_medical_knowledge,
            query_patient_history,
            query_department_info,
        )

        tools = [extract_symptoms, search_medical_knowledge, query_patient_history, query_department_info]

        user_message = state["messages"][-1].content if state["messages"] else ""
        session_id = state["session_id"]

        # ① 前置：TriageAgent 加载记忆 + 检索知识
        triage_agent = TriageAgent()
        memory = await triage_agent._load_memory(session_id)
        memory_context = memory.to_prompt_context()
        knowledge = await triage_agent._retrieve_knowledge("triage", user_message, top_k=3)
        knowledge_str = triage_agent._format_knowledge(knowledge, "（暂无匹配的参考知识，请基于通识推断）")

        patient_context = ""
        if state.get("patient_id"):
            pid = state["patient_id"]
            patient_context = chr(10) + "当前患者ID: " + str(pid) + "。如果需查病史，调用 query_patient_history。"

        # ② 核心：ReAct 多轮推理（system_prompt 注入记忆上下文）
        system_prompt = (
            "你是北京协和医院的AI分诊专家。你遵循 ReAct 工作模式：思考→行动→观察→循环。\n\n"
            "## 可用工具\n"
            "1. extract_symptoms — 从患者描述中提取结构化症状（必须第一步）\n"
            "2. search_medical_knowledge — 检索医学知识库（核心信息源，必须第二步）\n"
            "3. query_patient_history — 查询患者既往病史（仅在患者ID已知时使用）\n"
            "4. query_department_info — 验证科室（HIS不可用时自动回退到知识库，最多1次）\n\n"
            "## 追问决策（核心规则）\n"
            "你需要像真正的医生一样，先了解充分信息再下判断。\n\n"
            "必须追问的情况（needs_followup=true）：\n"
            "  - 患者只描述了1个症状，且没有说持续时间、严重程度、频率\n"
            "  - 症状太模糊（如'不舒服'、'难受'、'有点疼'）\n"
            "  - 患者没有提到任何伴随症状\n"
            "  - 知识库返回了多个可能的科室方向（>2个），信息不足以锁定\n\n"
            "可以给结论的情况（needs_followup=false）：\n"
            "  - 患者描述了2个以上明确症状，且说明了持续时间或严重程度\n"
            "  - 症状非常典型（如'流鼻涕、打喷嚏、鼻塞三天'），足以锁定科室\n\n"
            "重要规则：\n"
            "  - 绝大多数首次咨询都需要追问！只描述一个部位疼痛就要求判断科室是不负责任的行为\n"
            "  - 如需追问：在response_to_user中问2-3个具体问题，suggested_questions也填上\n"
            "  - 同时给出一个初步的recommended_department（你的最佳猜测），confidence设为0.5\n"
            "  - 使用友好温暖的语气，让患者感觉你是在关心他们，不是走流程\n"
            "  - 危急症状（胸痛、呼吸困难、大出血、意识模糊）立即标记为危急并建议急诊\n"
            "  - 最多调用3-4个工具，然后必须输出结果\n"
            "  - query_department_info 返回后不要再重复调用\n"
            + patient_context + "\n"
            "## 参考知识库\n" + knowledge_str + "\n\n"
            "---\n## 患者历史对话\n" + memory_context + "\n\n"
            "## 输出格式\n"
            "输出纯JSON（不要markdown代码块）：\n"
            '{"recommended_department": "初步判断的科室", "alternative_department": "备选科室", '
            '"confidence": 0.5, "urgency": "普通|紧急|危急", '
            '"reasoning": "你的分诊推理过程", "suggested_questions": ["追问1", "追问2"], '
            '"needs_followup": true, '
            '"response_to_user": "你给患者的回复。如果是追问，请在这里友好地问问题。"}'
        )

        graph = build_react_agent(tools, system_prompt, max_iterations=4, temperature=0.3)

        loop = asyncio.get_event_loop()
        result = await loop.run_in_executor(
            None,
            lambda: graph.invoke({
                "messages": [HumanMessage(content=user_message)],
                "iteration_count": 0,
            })
        )

        parsed = parse_json_result(result)
        state["triage_result"] = parsed
        state["symptoms"] = user_message

        # ③ 后置：写入 TriageAgent 隔离记忆
        response_text = parsed.get("response_to_user", parsed.get("recommended_department", ""))
        await triage_agent.remember(session_id, user_message, response_text)

        log.info(f"ReAct 分诊完成 - iterations: {result.get('iteration_count', '?')}, "
                 f"推荐科室: {parsed.get('recommended_department', '内科')}, needs_followup: {parsed.get('needs_followup', False)}")

    except Exception as e:
        log.error(f"ReAct 分诊失败: {str(e)}")
        state["error"] = f"分诊失败: {str(e)}"
        state["triage_result"] = {
            "recommended_department": "内科",
            "confidence": 0.5,
            "urgency": "普通",
            "reasoning": "分诊分析异常",
            "response_to_user": "抱歉，分诊分析暂时不可用，建议前往医院导诊台咨询。",
        }

    return state

async def register_node(state: GraphState) -> GraphState:
    """挂号节点"""
    log.info(f"执行挂号节点 - session_id: {state['session_id']}")

    try:
        from app.core.services.register_service import RegisterService
        register_service = RegisterService()

        # 从消息中提取挂号信息
        last_message = state['messages'][-1].content

        # 从分诊结果中预填科室信息
        pre_filled = {}
        triage_result = state.get('triage_result', {}) or {}
        if triage_result.get('recommended_department'):
            pre_filled['department'] = triage_result['recommended_department']
            log.info(f"预填科室: {pre_filled['department']}（来自分诊结果）")

        result = await register_service.process_register_request(
            session_id=state['session_id'],
            message=last_message,
            patient_id=state.get('patient_id'),
            pre_filled=pre_filled,
        )

        state['register_result'] = result.model_dump()

        # RegisterAgent 返回 None 表示它不处理（挂号已完成且用户不是说挂号相关的话）
        # → 交给 LLM 生成普通聊天回复
        if result.message is None or result.message == "":
            log.info(f"RegisterAgent 不处理此消息，交由 LLM 生成回复")
            from app.core.services.intent_service import IntentService
            intent_service = IntentService()
            state['response'] = await intent_service._generate_chat_response(state)
            state['register_result'] = {"success": False, "message": state['response']}
            return state

        log.info(f"挂号完成 - success: {result.success}")

    except Exception as e:
        log.error(f"挂号失败: {str(e)}")
        state['error'] = f"挂号失败: {str(e)}"

    return state


async def report_node(state: GraphState) -> GraphState:
    """报告解读节点 — 三明治模式：ReportAgent 记忆管理 + ReAct 多轮推理"""
    log.info(f"执行 ReAct 报告解读节点 - session_id: {state['session_id']}")

    try:
        from app.core.agents import ReportAgent
        from app.core.graph.react_agent import build_react_agent, parse_json_result
        from app.core.tools.medical_tools import (
            extract_symptoms,
            search_medical_knowledge,
            query_patient_history,
            query_medication_info,
        )

        tools = [extract_symptoms, search_medical_knowledge, query_patient_history, query_medication_info]

        user_message = state["messages"][-1].content if state["messages"] else ""
        session_id = state["session_id"]

        # ① 前置：检查是否有 HIS 推送来的缓存报告数据
        patient_id = state.get("patient_id")
        if patient_id:
            try:
                from app.utils.cache import MongoDBCache
                cache = MongoDBCache()
                cached = await cache.get_session_state(f"report_result:{patient_id}")
                if cached and cached.get("summary"):
                    log.info(f"[ReportNode] 使用缓存报告数据 - patient_id: {patient_id}, report: {cached.get('report_name')}")
                    state["report_result"] = cached
                    state["response"] = (
                        f"📊 您的【{cached.get('report_name', '报告')}】AI 解读如下：\n\n"
                        f"{cached.get('summary', '')}\n"
                    )
                    if cached.get("abnormal_indicators"):
                        for item in cached["abnormal_indicators"]:
                            name = item.get("name", item.get("indexName", ""))
                            val = item.get("value", "")
                            ref = item.get("reference", "")
                            interp = item.get("interpretation", item.get("clinical_significance", ""))
                            state["response"] += f"\n⚠️ {name}: {val}（参考: {ref}）→ {interp}"
                    if cached.get("recommendations"):
                        state["response"] += "\n\n💡 健康建议："
                        for rec in cached["recommendations"]:
                            state["response"] += f"\n  · {rec}"
                    return state
            except Exception as e:
                log.warning(f"[ReportNode] 读取缓存报告失败: {e}")

            from app.integrations.hospital.http_client import HospitalAPIClient
            import json
            client = HospitalAPIClient()
            try:
                reports = await client.get_exam_results_by_patient(int(patient_id))
            finally:
                await client.close()
            if reports:
                latest = reports[0]
                report_data = {
                    "report_name": latest.get("inspectName", "检查报告"),
                    "item_type": latest.get("itemType", "NUMERIC"),
                    "summary": latest.get("summaryConclusion", ""),
                    "indicators": latest.get("indices", []),
                    "check_content": (latest.get("imageText") or {}).get("checkContent", ""),
                    "check_conclusion": (latest.get("imageText") or {}).get("checkConclusion", ""),
                }
                state["report_result"] = await ReportAgent().interpret(
                    json.dumps(report_data, ensure_ascii=False, default=str),
                    session_id=f"{patient_id}:report:{latest.get('id')}",
                )
                return state

        # ② 前置：ReportAgent 加载记忆 + 检索知识
        report_agent = ReportAgent()
        memory = await report_agent._load_memory(session_id)
        memory_context = memory.to_prompt_context()
        knowledge = await report_agent._retrieve_knowledge("report", user_message, top_k=3)
        knowledge_str = report_agent._format_knowledge(knowledge, "（暂无匹配的参考知识库数据）")

        patient_context = ""
        if state.get("patient_id"):
            patient_context = f"\n当前患者ID: {state['patient_id']}。如需查病史，调用 query_patient_history。"

        # ③ 核心：ReAct 多轮推理（system_prompt 注入记忆上下文 + 知识库）
        system_prompt = (
            "你是北京协和医院的AI报告解读专家。你遵循 ReAct 工作模式：思考→行动→观察→循环。\n\n"
            "## 可用工具\n"
            "1. extract_symptoms — 从报告文本中提取结构化检测指标（指标名、检测值、单位、参考范围、是否异常）\n"
            "2. search_medical_knowledge — 检索异常指标的医学知识（系统已预检索知识库，优先参考；如需补充再调用）\n"
            "3. query_patient_history — 查询患者既往病史\n"
            "4. query_medication_info — 查询药物信息\n\n"
            "## 工作流程\n"
            "1. extract_symptoms → 提取所有指标，识别异常项\n"
            "2. 结合参考知识库分析每个异常指标（参考知识库已预检索，优先使用）\n"
            "3. 如需要，调用 query_patient_history 或 query_medication_info 一次\n"
            "4. 综合所有信息立即输出结果\n\n"
            "## 关键规则\n"
            "- 最多调用2个工具，然后必须输出JSON结果，不要反复调用同一个工具\n"
            "- 不做最终疾病诊断，解读仅供参考\n"
            "- 对危急值标记并建议立即就医\n"
            "- **领域约束**：只解读报告中实际出现的检查项目，不要引入未出现的指标（如肝功能、尿酸、血脂等）。如果报告标注了检查名称（如'鼻炎检查'），解读必须围绕该领域，不扩展到无关器官系统\n"
            f"{patient_context}\n"
            "## 参考知识库\n" + knowledge_str + "\n\n"
            "---\n## 患者历史对话\n" + memory_context + "\n\n"
            "## 输出格式\n"
            "输出纯JSON（不要markdown代码块）：\n"
            '{"summary": "整体解读（2-3句，必须提及检查项目名称）", '
            '"abnormal_indicators": [{"name": "指标名", "value": "检测值", "unit": "单位", '
            '"reference_range": "参考范围", "interpretation": "解读"}], '
            '"recommendations": ["建议1", "建议2"], '
            '"response_to_user": "给患者的完整回复"}'
        )

        graph = build_react_agent(tools, system_prompt, max_iterations=4, temperature=0.3)

        loop = asyncio.get_event_loop()
        result = await loop.run_in_executor(
            None,
            lambda: graph.invoke({
                "messages": [HumanMessage(content=user_message)],
                "iteration_count": 0,
            })
        )

        parsed = parse_json_result(result)
        state["report_result"] = parsed

        # ③ 后置：写入 ReportAgent 隔离记忆
        await report_agent.remember(session_id, user_message[:200], parsed.get("summary", ""))

        log.info(f"ReAct 报告解读完成 - iterations: {result.get('iteration_count', '?')}, "
                 f"summary: {parsed.get('summary', '')[:50]}...")

    except Exception as e:
        log.error(f"ReAct 报告解读失败: {str(e)}")
        state["error"] = f"报告解读失败: {str(e)}"
        state["report_result"] = {
            "summary": "报告解读暂时不可用，请稍后重试或咨询医生。",
            "abnormal_indicators": [],
            "recommendations": ["请携带报告原件就医"],
        }

    return state


async def medication_node(state: GraphState) -> GraphState:
    """用药提醒节点 — 优先读缓存处方数据，无缓存才调 MedicationAgent"""
    log.info(f"执行用药提醒节点 - session_id: {state['session_id']}")

    try:
        # ① 前置：查 HIS 推送来的处方缓存
        patient_id = state.get("patient_id")
        if patient_id:
            try:
                from app.utils.cache import MongoDBCache
                cache = MongoDBCache()
                cached = await cache.get_session_state(f"medication_result:{patient_id}")
                if cached and cached.get("analysis"):
                    analysis = cached["analysis"]
                    meds = cached.get("medications", [])
                    med_names = ", ".join(m.get("name", "") for m in meds) if meds else "未知药品"
                    state["medication_result"] = analysis
                    safe_status = "未发现明显药物相互作用" if analysis.get("safe") else "发现潜在药物相互作用"
                    state["response"] = (
                        f"💊 您的处方已开具【{med_names}】\n\n"
                        f"AI 用药分析：{safe_status}\n\n"
                    )
                    if analysis.get("warnings"):
                        for w in analysis["warnings"]:
                            state["response"] += f"⚠️ {w}\n"
                    if analysis.get("interactions"):
                        state["response"] += "\n药物相互作用：\n"
                        for inter in analysis["interactions"]:
                            state["response"] += f"  · {inter.get('drug_a', '')} × {inter.get('drug_b', '')}: {inter.get('description', '')}\n"
                    log.info(f"用药节点 — 使用缓存数据，药品: {med_names}")
                    return state
            except Exception as e:
                log.warning(f"[MedicationNode] 读取缓存处方失败: {e}")

        # ② 无缓存 → 直调 MedicationAgent
        from app.core.agents import MedicationAgent
        agent = MedicationAgent()

        last_message = state['messages'][-1].content
        result = await agent.analyze(
            medications=[last_message],
            patient_id=state.get('patient_id'),
            session_id=state['session_id'],
        )

        state['medication_result'] = result

        log.info(f"用药检查完成 - safe: {result.get('safe')}")

    except Exception as e:
        log.error(f"用药检查失败: {str(e)}")
        state['error'] = f"用药检查失败: {str(e)}"

    return state


async def hospital_info_node(state: GraphState) -> GraphState:
    """医院信息查询节点"""
    log.info(f"执行医院信息查询节点 - session_id: {state['session_id']}")

    try:
        from app.core.agents import HospitalAgent
        agent = HospitalAgent()

        last_message = state['messages'][-1].content
        reply = await agent.query_hospital_info(last_message, session_id=state['session_id'])

        state['response'] = reply
        state['metadata']['hospital_info_queried'] = True

        log.info(f"医院信息查询完成")

    except Exception as e:
        log.error(f"医院信息查询失败: {str(e)}")
        state['error'] = f"医院信息查询失败: {str(e)}"
        state['response'] = "抱歉，暂时无法查询医院信息，请稍后重试。"

    return state


async def examination_event_node(state: GraphState) -> GraphState:
    """检查开单事件节点 — 医生开具检查后自动触发"""
    log.info(f"执行检查开单事件节点 - session_id: {state['session_id']}")

    try:
        from app.core.events.event_handler import handle_examination_ordered
        from app.core.events.event_models import ExaminationOrderedEvent

        event_data = state.get('event_data', {})
        event = ExaminationOrderedEvent(**event_data)
        await handle_examination_ordered(event)

        state['response'] = (
            f"已收到检查开单通知。"
            f"AI 将为患者 {event.patient_id} 安排取报告和复诊提醒。"
        )

    except Exception as e:
        log.error(f"检查开单事件处理失败: {str(e)}")
        state['error'] = f"检查开单事件处理失败: {str(e)}"

    return state


async def report_event_node(state: GraphState) -> GraphState:
    """报告生成事件节点 — 报告生成后自动触发 AI 解读"""
    log.info(f"执行报告生成事件节点 - session_id: {state['session_id']}")

    try:
        from app.core.events.event_handler import handle_report_generated
        from app.core.events.event_models import ReportGeneratedEvent

        event_data = state.get('event_data', {})
        event = ReportGeneratedEvent(**event_data)
        await handle_report_generated(event)

        state['response'] = (
            f"报告已解读完成，结果已推送给患者 {event.patient_id}。"
        )

    except Exception as e:
        log.error(f"报告生成事件处理失败: {str(e)}")
        state['error'] = f"报告生成事件处理失败: {str(e)}"

    return state


async def prescription_event_node(state: GraphState) -> GraphState:
    """处方创建事件节点 — 医生开方后自动触发用药分析"""
    log.info(f"执行处方创建事件节点 - session_id: {state['session_id']}")

    try:
        from app.core.events.event_handler import handle_prescription_created
        from app.core.events.event_models import PrescriptionCreatedEvent

        event_data = state.get('event_data', {})
        event = PrescriptionCreatedEvent(**event_data)
        await handle_prescription_created(event)

        state['response'] = (
            f"处方用药分析已完成，用药提醒已启动，"
            f"结果已推送给患者 {event.patient_id}。"
        )

    except Exception as e:
        log.error(f"处方创建事件处理失败: {str(e)}")
        state['error'] = f"处方创建事件处理失败: {str(e)}"

    return state



async def response_generation_node(state: GraphState) -> GraphState:
    """响应生成节点"""
    log.info(f"执行响应生成节点 - session_id: {state['session_id']}")

    try:
        # 分诊追问场景：直接用 triage 的 response_to_user
        triage_result = state.get('triage_result', {}) or {}
        if triage_result.get('needs_followup'):
            state['response'] = triage_result.get('response_to_user', '')
            log.info(f"分诊追问模式 - 直接使用 triage response_to_user")
            return state

        from app.core.services.intent_service import IntentService
        intent_service = IntentService()

        response = await intent_service.generate_response(state)
        state['response'] = response
        log.info(f"响应生成完成 - response: {response[:50]}...")

    except Exception as e:
        log.error(f"响应生成失败: {str(e)}")
        state['error'] = f"响应生成失败: {str(e)}"
        state['response'] = "抱歉，我无法处理您的请求，请稍后重试。"

    return state


def route_intent(state: GraphState) -> str:
    """意图路由函数"""
    intent = state.get('intent')
    event_type = state.get('event_type')

    # 事件驱动的路由（优先级更高）
    if event_type == "examination_ordered":
        return "examination_event"
    elif event_type == "report_generated":
        return "report_event"
    elif event_type == "prescription_created":
        return "prescription_event"

    if intent == IntentType.TRIAGE:
        return "triage"
    elif intent == IntentType.REGISTER:
        return "register"
    elif intent == IntentType.REPORT:
        return "report"
    elif intent == IntentType.MEDICATION:
        return "medication"
    elif intent == IntentType.HOSPITAL:
        return "hospital_info"
    else:
        return "response_generation"


def create_diagnosis_graph() -> StateGraph:
    """创建诊断工作流图"""

    workflow = StateGraph(GraphState)

    # 添加节点
    workflow.add_node("intent_recognition", intent_recognition_node)
    workflow.add_node("triage", triage_node)
    workflow.add_node("register", register_node)
    workflow.add_node("report", report_node)
    workflow.add_node("medication", medication_node)
    workflow.add_node("hospital_info", hospital_info_node)
    workflow.add_node("examination_event", examination_event_node)
    workflow.add_node("report_event", report_event_node)
    workflow.add_node("prescription_event", prescription_event_node)
    workflow.add_node("response_generation", response_generation_node)

    # 设置入口点
    workflow.set_entry_point("intent_recognition")

    # 添加条件边
    workflow.add_conditional_edges(
        "intent_recognition",
        route_intent,
        {
            "triage": "triage",
            "register": "register",
            "report": "report",
            "medication": "medication",
            "hospital_info": "hospital_info",
            "examination_event": "examination_event",
            "report_event": "report_event",
            "prescription_event": "prescription_event",
            "response_generation": "response_generation",
        }
    )

    # 业务节点到响应生成节点
    workflow.add_edge("triage", "response_generation")
    workflow.add_edge("register", "response_generation")
    workflow.add_edge("report", "response_generation")
    workflow.add_edge("medication", "response_generation")
    workflow.add_edge("hospital_info", "response_generation")
    workflow.add_edge("examination_event", "response_generation")
    workflow.add_edge("report_event", "response_generation")
    workflow.add_edge("prescription_event", "response_generation")

    # 结束
    workflow.add_edge("response_generation", END)

    return workflow


# ── 挂号会话检测 ──────────────────────────────────────────

async def _check_active_register(session_id: str) -> bool:
    """检查当前 session 是否有活跃的挂号流程（未完成）"""
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        state = await cache.get_session_state(f"register_state:{session_id}")
        if state:
            step = state.get("step", "")
            collected = state.get("collected_info", {})
            if step in ("collect_info", "choose_doctor", "confirm_register", "confirm_cancel"):
                if any(collected.values()):
                    return True
        return False
    except Exception:
        return False


async def _clear_register_state(session_id: str):
    """清除活跃挂号状态（用户想切换话题时调用）"""
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_session_state(f"register_state:{session_id}", {"step": "done", "collected_info": {}})
        log.info(f"已清除挂号会话状态 - session_id: {session_id}")
    except Exception as e:
        log.error(f"清除挂号状态失败: {e}")
