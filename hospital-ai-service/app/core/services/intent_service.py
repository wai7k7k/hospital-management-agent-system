"""
中央大脑 — IntentService（意图识别 + 响应格式化）

IntentService 负责两个核心职责：
  1. recognize_intent() — 解析用户消息，路由到 LangGraph 对应节点
  2. generate_response() — 工作流最后一环，从各节点结果拼装最终回复

Agent 调用由 LangGraph 各节点直接完成（TriageAgent / ReportAgent / MedicationAgent 等），
不再经过 IntentService 中转调度。
"""

import json
from typing import Dict, Any
from datetime import date

from dashscope import Generation
from app.utils.config import settings
from app.utils.logger import log
from app.core.models.request import IntentType
from app.core.prompts import render
from app.core.memory import get_memory_provider


class IntentService:
    """中央调度大脑"""

    AGENT_NAME = "intent"

    def __init__(self):
        self.api_key = settings.DASH_SCOPE_API_KEY
        self.model = settings.QWEN_MODEL
        self.temperature = settings.QWEN_TEMPERATURE
        self.max_tokens = settings.QWEN_MAX_TOKENS
        self._memory_provider = get_memory_provider(max_messages=20)

    # ── 记忆辅助 ──────────────────────────────────────────

    async def _get_memory(self, session_id: str):
        return await self._memory_provider.memory_for(self.AGENT_NAME, session_id).load()

    # ── 意图识别 ──────────────────────────────────────────

    async def recognize_intent(self, message: str, history: list = None) -> Dict[str, Any]:
        """
        中央大脑识别患者意图（兼容旧接口）

        Returns:
            {"intent": "triage", "confidence": 0.95, "metadata": {...}}
        """
        rule_result = self._recognize_by_rules(message)
        if rule_result:
            log.info(
                f"本地意图识别 - message: {message[:30]}... → "
                f"{rule_result['intent']} (confidence: {rule_result['confidence']})"
            )
            return rule_result

        try:
            history_text = ""
            if history:
                recent = history[-6:]
                lines = []
                for m in recent:
                    role_label = "患者" if (hasattr(m, 'role') and m.role == "user") else "助手"
                    content = m.content if hasattr(m, 'content') else str(m)
                    lines.append(f"{role_label}: {content}")
                if lines:
                    history_text = "对话历史:\n" + "\n".join(lines) + "\n\n"

            prompt = render("intent", current_date=date.today().isoformat())
            full_prompt = f"{prompt}\n\n{history_text}患者最新消息: {message}"

            response = Generation.call(
                api_key=self.api_key,
                model=self.model,
                prompt=full_prompt,
                max_tokens=self.max_tokens,
                temperature=self.temperature,
            )

            result = self._parse_intent_response(response)
            log.info(f"意图识别 - message: {message[:30]}... → {result['intent']} (confidence: {result['confidence']})")
            return result

        except Exception as e:
            log.error(f"意图识别失败: {str(e)}")
            return {"intent": IntentType.CHAT, "confidence": 0.0, "metadata": {"error": str(e)}}

    @staticmethod
    def _recognize_by_rules(message: str) -> Dict[str, Any] | None:
        """对明确业务指令做本地路由，避免外部模型异常时退化成无意义闲聊。"""
        text = str(message or "").strip().lower()
        if not text:
            return None

        keyword_groups = (
            (IntentType.REGISTER, ("挂号", "预约科室", "预约医生", "预约门诊")),
            (IntentType.REPORT, ("报告解读", "分析报告", "检查结果", "化验结果", "检验结果", "报告出来")),
            (IntentType.MEDICATION, ("用药", "吃药", "服药", "药怎么", "处方", "药物")),
            (
                IntentType.TRIAGE,
                (
                    "不舒服", "哪里疼", "疼痛", "发烧", "发热", "咳嗽", "头晕",
                    "恶心", "呕吐", "腹泻", "胸闷", "心慌", "视物", "眼睛疼",
                    "眼睛痛", "眼睛痒", "眼睛红", "看不清",
                ),
            ),
        )
        for intent, keywords in keyword_groups:
            if any(keyword in text for keyword in keywords):
                return {
                    "intent": intent,
                    "confidence": 0.99,
                    "metadata": {"source": "local_rules"},
                }
        return None

    def _parse_intent_response(self, response) -> Dict[str, Any]:
        try:
            if response.status_code == 200:
                content = response.output.text
                start_idx = content.find("{")
                end_idx = content.rfind("}") + 1
                if start_idx != -1 and end_idx > start_idx:
                    data = json.loads(content[start_idx:end_idx])

                    agent = data.get("agent", "chat")
                    agent_to_intent = {
                        "triage": IntentType.TRIAGE,
                        "register": IntentType.REGISTER,
                        "report": IntentType.REPORT,
                        "medication": IntentType.MEDICATION,
                        "chat": IntentType.CHAT,
                    }
                    intent = agent_to_intent.get(agent, IntentType.CHAT)

                    return {
                        "intent": intent,
                        "confidence": data.get("confidence", 0.8),
                        "metadata": {
                            "reasoning": data.get("reasoning", ""),
                            "agent": agent,
                            "extracted_info": data.get("extracted_info", {}),
                            "response_to_user": data.get("response_to_user", ""),
                            "raw_response": content,
                        },
                    }
            log.warning(
                f"意图模型返回异常状态 - status_code={getattr(response, 'status_code', None)}, "
                f"code={getattr(response, 'code', None)}, message={getattr(response, 'message', None)}"
            )
            return {"intent": IntentType.CHAT, "confidence": 0.0, "metadata": {"model_unavailable": True}}
        except Exception as e:
            log.error(f"解析意图响应失败: {str(e)}")
            return {"intent": IntentType.CHAT, "confidence": 0.0, "metadata": {"error": str(e)}}

    # ── 响应生成（LangGraph 工作流最后一环） ────────────────

    async def generate_response(self, state: Dict[str, Any]) -> str:
        intent = state.get("intent")
        if intent == IntentType.TRIAGE:
            return self._format_triage_response(state)
        elif intent == IntentType.REGISTER:
            return self._format_register_response(state)
        elif intent == IntentType.REPORT:
            return self._format_report_response(state)
        elif intent == IntentType.MEDICATION:
            return self._format_medication_response(state)
        elif intent == IntentType.HOSPITAL:
            return state.get("response", "您好！有什么关于医院的问题我可以帮您解答吗？")
        else:
            return await self._generate_chat_response(state)
    def _format_triage_response(self, state: Dict[str, Any]) -> str:
        r = state.get("triage_result", {})
        dept = r.get("recommended_department", "内科")
        urgency = r.get("urgency", "普通")
        reasoning = r.get("reasoning", "")
        return f"🩺 根据您的症状分析，建议挂【{dept}】\n紧急程度: {urgency}\n\n{reasoning}\n\n需要我帮您挂号吗？😊"

    def _format_register_response(self, state: Dict[str, Any]) -> str:
        r = state.get("register_result") or {}
        return r.get("message", "挂号处理中，请稍候...")

    def _format_report_response(self, state: Dict[str, Any]) -> str:
        r = state.get("report_result", {})
        if not r:
            return "抱歉，报告解读失败，请提供清晰的报告数据。"
        resp = f"📊 {r.get('summary', '')}\n\n"
        for item in r.get("abnormal_indicators", []):
            resp += f"⚠️ {item.get('name', '')}: {item.get('description', '')}\n"
        for rec in r.get("recommendations", []):
            resp += f"💡 {rec}\n"
        return resp

    def _format_medication_response(self, state: Dict[str, Any]) -> str:
        r = state.get("medication_result", {})
        if r.get("safe"):
            return "✅ 未发现明显药物相互作用，请按医嘱服用。"
        resp = "⚠️ 用药提醒:\n\n"
        for inter in r.get("interactions", []):
            resp += f"  {inter.get('drug_a','')} × {inter.get('drug_b','')}: {inter.get('description','')} ({inter.get('severity','')})\n"
        for w in r.get("warnings", []):
            resp += f"  - {w}\n"
        return resp

    async def _generate_chat_response(self, state: Dict[str, Any]) -> str:
        messages = state.get("messages", [])
        last_msg = messages[-1].content if messages else ""
        history = messages[-7:-1]
        history_text = "\n".join(
            f"{'患者' if getattr(item, 'role', '') == 'user' else '助手'}：{getattr(item, 'content', '')}"
            for item in history
        )
        try:
            response = Generation.call(
                api_key=self.api_key,
                model=self.model,
                prompt=(
                    '你是医院管理系统内的智能助手“小智”。结合最近对话，直接、简短回答当前问题。\n'
                    '禁止虚构医院名称、电话、公众号、APP、就诊渠道或疫情要求；禁止重复问候和无关温馨提示。\n'
                    '如果信息不足，只追问完成当前任务所必需的一项信息。回复控制在120字以内。\n\n'
                    f'最近对话：\n{history_text}\n患者当前消息：{last_msg}'
                ),
                max_tokens=180,
                temperature=0.2,
            )
            if response.status_code == 200:
                return response.output.text
            log.warning(
                f"聊天模型返回异常状态 - status_code={getattr(response, 'status_code', None)}, "
                f"code={getattr(response, 'code', None)}, message={getattr(response, 'message', None)}"
            )
            return "AI 模型暂时不可用，请稍后重试。"
        except Exception as e:
            log.error(f"生成聊天回复失败: {e}")
            return "抱歉，请稍后重试。"
