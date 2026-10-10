"""
医疗 ReAct 工具箱

LangChain @tool 装饰的工具函数，供 ReAct Agent 在思考-行动-观察循环中调用。
每个工具独立初始化所需的外部服务（Pinecone、HIS HTTP、DashScope）。
"""

from langchain_core.tools import tool


# ═══════════════════════════════════════════════════════════════
# 1. 症状提取
# ═══════════════════════════════════════════════════════════════

@tool
def extract_symptoms(text: str) -> str:
    """
    从患者的自然语言描述中提取结构化症状信息。
    当需要将患者的非结构化描述转换为标准症状列表时调用。
    输入: 患者的原始症状描述文本
    返回: JSON格式的结构化症状数据 {symptoms: [...], duration: "...", severity: "..."}
    """
    from dashscope import Generation
    from app.utils.config import settings
    import json

    response = Generation.call(
        api_key=settings.DASH_SCOPE_API_KEY,
        model=settings.QWEN_MODEL,
        prompt=(
            f"从以下患者描述中提取症状信息，返回纯JSON：\n"
            f'{{"symptoms": ["症状1", "症状2"], "duration": "持续多久", '
            f'"severity": "mild/moderate/severe", "accompanying": ["伴随症状"]}}\n\n'
            f"患者描述: {text}"
        ),
        max_tokens=300,
        temperature=0.1,
    )
    if response.status_code == 200:
        content = response.output.text
        s = content.find("{")
        e = content.rfind("}") + 1
        if s != -1 and e > s:
            return content[s:e]
    return json.dumps({"symptoms": [text], "duration": "未知", "severity": "moderate", "accompanying": []})


# ═══════════════════════════════════════════════════════════════
# 2. 患者病史查询
# ═══════════════════════════════════════════════════════════════

@tool
def query_patient_history(patient_id: str) -> str:
    """
    查询患者的既往病史和就诊记录。
    当需要了解患者历史健康状况、既往诊断或用药史时调用。
    输入: patient_id 患者ID
    返回: JSON格式的患者历史记录
    """
    import json, asyncio
    from app.integrations.hospital.http_client import HospitalAPIClient

    async def _query():
        client = HospitalAPIClient()
        try:
            pid = int(patient_id) if patient_id.isdigit() else None
            if not pid:
                return json.dumps({"error": f"无效的患者ID: {patient_id}"})

            registers = await client.get_register_list(patient_id=pid)
            prescriptions = await client.get_prescriptions()
            inspect_orders = await client.get_inspect_orders(patient_id=pid)

            return json.dumps({
                "patient_id": patient_id,
                "visit_count": len(registers) if registers else 0,
                "recent_visits": registers[:5] if registers else [],
                "prescription_count": len(prescriptions) if prescriptions else 0,
                "recent_prescriptions": [{
                    "medication": m.get("medicineName", ""),
                    "dosage": m.get("dosage", ""),
                } for p in prescriptions[:3] if isinstance(p, dict) for m in (p.get("prescriptionList", []) if isinstance(p.get("prescriptionList"), list) else [p])],
                "inspect_count": len(inspect_orders) if inspect_orders else 0,
            }, ensure_ascii=False)
        except Exception as e:
            return json.dumps({"error": f"查询病史失败: {str(e)}"})
        finally:
            await client.close()

    loop = asyncio.new_event_loop()
    try:
        return loop.run_until_complete(_query())
    finally:
        loop.close()


# ═══════════════════════════════════════════════════════════════
# 3. 医学知识检索
# ═══════════════════════════════════════════════════════════════

@tool
def search_medical_knowledge(query: str) -> str:
    """
    从医学知识库中检索症状对应的可能疾病、推荐科室和紧急程度。
    当需要根据症状查找匹配的医学知识时调用。可分诊和报告解读两个场景。
    输入: query 检索查询文本（症状描述或指标名称）
    返回: JSON格式的匹配知识条目列表
    """
    import json, asyncio
    from app.integrations.vector.pinecone_client import PineconeClient

    async def _search():
        client = PineconeClient()
        try:
            # 同时搜索 triage 和 report 两个命名空间
            triage_results = await client.search("triage", query, top_k=3)
            report_results = await client.search("report", query, top_k=2)

            all_results = []
            for r in triage_results:
                r["source"] = "triage_kb"
                all_results.append(r)
            for r in report_results:
                r["source"] = "report_kb"
                all_results.append(r)

            if not all_results:
                return json.dumps({"results": [], "note": "未找到匹配的医学知识条目，请基于医学常识推断"})

            return json.dumps({"results": all_results[:5]}, ensure_ascii=False)
        except Exception as e:
            return json.dumps({"results": [], "error": str(e)})

    loop = asyncio.new_event_loop()
    try:
        return loop.run_until_complete(_search())
    finally:
        loop.close()


# ═══════════════════════════════════════════════════════════════
# 4. 科室信息查询
# ═══════════════════════════════════════════════════════════════

@tool
def query_department_info(department_name: str) -> str:
    """
    查询医院科室信息，验证科室是否存在并获取科室详情。
    当需要确认推荐科室是否真实存在、或获取科室介绍时调用。
    输入: department_name 科室名称（如"神经内科"）
    返回: JSON格式的科室信息。

    注意：如果HIS系统不可用，会自动回退到Pinecone知识库查询，
    绝不会让HIS的失败阻碍分诊流程。调用者应信任返回结果并继续推进。
    """
    import json, asyncio
    from app.integrations.hospital.http_client import HospitalAPIClient

    async def _query():
        # 1. 先尝试 HIS
        client = HospitalAPIClient()
        try:
            result = await client.get_department(department_name)
            if result:
                return json.dumps({
                    "found": True,
                    "source": "HIS",
                    "department": result,
                }, ensure_ascii=False)
        except Exception:
            pass  # HIS 不可用，继续走后备
        finally:
            await client.close()

        # 2. HIS 失败 → 搜索 Pinecone hospital 知识库
        try:
            from app.integrations.vector.pinecone_client import PineconeClient
            pc = PineconeClient()
            results = await pc.search("hospital", department_name, top_k=3)
            if results:
                best = results[0]
                return json.dumps({
                    "found": True,
                    "source": "Pinecone知识库",
                    "department": {
                        "name": department_name,
                        "description": best.get("description", best.get("text", ""))[:300],
                        "filename": best.get("filename", ""),
                    },
                    "note": "HIS系统当前不可用，以上信息来自医院知识库，可用于分诊推荐",
                }, ensure_ascii=False)
        except Exception:
            pass

        # 3. 全部失败 → 告诉 agent 不要纠结，用已有知识继续
        return json.dumps({
            "found": None,
            "source": "none",
            "department_name": department_name,
            "note": "HIS和知识库均不可用。请基于你的医学常识直接推荐该科室，不要重复查询此工具。如果这是常见标准科室（如内科、外科、儿科），直接认为其存在。",
        })

    loop = asyncio.new_event_loop()
    try:
        return loop.run_until_complete(_query())
    finally:
        loop.close()


# ═══════════════════════════════════════════════════════════════
# 5. 药物信息查询
# ═══════════════════════════════════════════════════════════════

@tool
def query_medication_info(medication_name: str) -> str:
    """
    查询药物详细信息，包括适应症、用法用量、副作用和禁忌症。
    当需要了解具体药物的安全信息时调用。
    输入: medication_name 药品名称
    返回: JSON格式的药品信息
    """
    import json, asyncio
    from app.integrations.hospital.http_client import HospitalAPIClient

    async def _query():
        client = HospitalAPIClient()
        try:
            result = await client.get_medication_info(medication_name)
            if result:
                return json.dumps({"found": True, "medication": result}, ensure_ascii=False)
            return json.dumps({"found": False, "medication_name": medication_name})
        except Exception as e:
            return json.dumps({"found": False, "error": str(e)})
        finally:
            await client.close()

    loop = asyncio.new_event_loop()
    try:
        return loop.run_until_complete(_query())
    finally:
        loop.close()


# ═══════════════════════════════════════════════════════════════
# 工具集导出
# ═══════════════════════════════════════════════════════════════

TRIAGE_TOOLS = [
    extract_symptoms,
    search_medical_knowledge,
    query_patient_history,
    query_department_info,
]

REPORT_TOOLS = [
    extract_symptoms,
    search_medical_knowledge,
    query_patient_history,
    query_medication_info,
]

ALL_TOOLS = TRIAGE_TOOLS + [
    query_medication_info,
]
