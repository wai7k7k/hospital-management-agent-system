"""
ReAct 分诊独立测试

只測 ReAct 子图，不经过完整的 LangGraph 父图，
方便快速验证 agent 的思考→行动→观察循环。
"""

import sys
import io
import json
import time
from pathlib import Path

# 添加项目根目录到路径
sys.path.insert(0, str(Path(__file__).parent.parent))

# 确保控制台用 UTF-8，避免 emoji 乱码
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8")

from langchain_core.messages import HumanMessage
from app.core.graph.react_agent import build_react_agent, parse_json_result
from app.core.tools.medical_tools import (
    extract_symptoms,
    search_medical_knowledge,
    query_patient_history,
    query_department_info,
)

# ── 测试用例 ────────────────────────────────────────────────

TEST_CASES = [
    {
        "label": "基础分诊：头痛发热",
        "message": "头痛发热三天了，体温38度，应该挂什么科？",
        "patient_id": "1",
    },
    {
        "label": "危急症状：胸痛",
        "message": "胸口剧烈疼痛，呼吸困难，已经持续半小时了",
        "patient_id": None,
    },
    {
        "label": "复杂多症状",
        "message": "最近一个月反复胃痛，饭后加重，偶尔反酸，有时候恶心",
        "patient_id": None,
    },
]


def run_triage_test(message: str, patient_id: str = None, label: str = ""):
    """执行单次 ReAct 分诊测试"""

    tools = [
        extract_symptoms,
        search_medical_knowledge,
        query_patient_history,
        query_department_info,
    ]

    patient_context = ""
    if patient_id:
        patient_context = f"\n当前患者ID: {patient_id}。如需查病史请调用 query_patient_history。"

    system_prompt = (
        "你是北京协和医院的AI分诊专家。你遵循 ReAct 工作模式：思考→行动→观察→循环，直到获得足够信息。\n\n"
        "## 可用工具\n"
        "1. extract_symptoms — 从患者描述中提取结构化症状（症状列表、持续时间、严重程度）\n"
        "2. search_medical_knowledge — 根据症状检索医学知识库（匹配科室、疾病、紧急程度），这是最可靠的信息源\n"
        "3. query_patient_history — 查询患者既往病史和就诊记录\n"
        "4. query_department_info — 验证科室信息（HIS不可用时自动回退到知识库）\n\n"
        "## 工作流程（严格遵守）\n"
        "1. extract_symptoms → 提取症状（必须第一步）\n"
        "2. search_medical_knowledge → 检索匹配的科室（必须第二步，这是核心）\n"
        "3. query_department_info → 验证科室（最多调用1次，无论返回什么都接受结果）\n"
        "4. 基于以上信息，立即生成最终JSON结果\n\n"
        "## 关键规则\n"
        "- 最多调用3-4个工具，然后必须输出结果，不要反复验证同一个科室\n"
        "- query_department_info 如果返回 found=None 或 found=True，都直接接受，不要重试\n"
        "- 即使某个工具失败，也必须基于已有信息给出分诊结果\n"
        "- 危急症状（胸痛、呼吸困难、大出血、意识模糊）必须标记为「危急」并建议立即急诊\n"
        f"{patient_context}\n"
        "## 输出格式\n"
        "完成所有必要工具调用后，必须输出纯JSON（不要markdown代码块包裹）：\n"
        '{"recommended_department": "科室名", "alternative_department": "备选科室", '
        '"confidence": 0.92, "urgency": "普通|紧急|危急", '
        '"reasoning": "分诊依据", '
        '"response_to_user": "给患者的完整回复（友好、专业、含图标）"}'
    )

    graph = build_react_agent(tools, system_prompt, max_iterations=4, temperature=0.3)

    start = time.time()
    result = graph.invoke({
        "messages": [HumanMessage(content=message)],
        "iteration_count": 0,
    })
    elapsed = time.time() - start

    parsed = parse_json_result(result)

    # ── 打印结果 ──────────────────────────────────────────
    print()
    print("=" * 70)
    print(f"  {label}")
    print("=" * 70)
    print(f"  患者消息: {message}")
    print(f"  患者ID:   {patient_id or '(无)'}")
    print(f"  耗时:     {elapsed:.1f}s")
    print(f"  迭代次数: {result.get('iteration_count', '?')}")
    print("-" * 70)
    print(f"  推荐科室: {parsed.get('recommended_department', '?')}")
    print(f"  备选科室: {parsed.get('alternative_department', '?')}")
    print(f"  紧急程度: {parsed.get('urgency', '?')}")
    print(f"  置信度:   {parsed.get('confidence', '?')}")
    print(f"  分诊依据: {parsed.get('reasoning', '?')[:200]}")
    print("-" * 70)
    print(f"  回复患者: {parsed.get('response_to_user', '?')[:300]}")
    print()

    # ── 追踪工具调用链 ────────────────────────────────────
    messages = result.get("messages", [])
    tool_names = []
    for msg in messages:
        if hasattr(msg, "additional_kwargs"):
            fc = msg.additional_kwargs.get("function_call") if msg.additional_kwargs else None
            if fc and fc.get("name"):
                tool_names.append(fc["name"])
    print(f"  工具调用链: {' → '.join(tool_names) if tool_names else '(无工具调用)'}")
    print("=" * 70)
    print()

    return parsed


# ── 主入口 ──────────────────────────────────────────────────

if __name__ == "__main__":
    print("ReAct 分诊独立测试")
    print(f"测试用例数: {len(TEST_CASES)}")
    print()

    for tc in TEST_CASES:
        try:
            run_triage_test(
                message=tc["message"],
                patient_id=tc.get("patient_id"),
                label=tc["label"],
            )
        except Exception as e:
            print(f"  ❌ 测试失败: {e}")
