"""
ReAct Agent 子图构建器

在 LangGraph 父图中嵌入 ReAct 循环:
  agent (LLM + functions) ──→ tools ──→ agent ──→ ... ──→ END

使用场景：
  - triage_node: 分诊 ReAct (症状提取 → 知识检索 → 病史查询 → 科室验证)
  - report_node: 报告解读 ReAct (指标提取 → 知识检索 → 药物查询)

适配 langgraph 0.0.x + langchain-openai 0.0.x 旧版 API:
  - bind_functions 替代 bind_tools
  - additional_kwargs.function_call 替代 tool_calls
  - FunctionMessage 替代 ToolMessage
"""

import json
import re
from typing import List, Optional, Dict, Any, Callable
from typing_extensions import TypedDict, Annotated

from langgraph.graph import StateGraph, END
from langgraph.graph.message import add_messages
from langchain_openai import ChatOpenAI
from langchain_core.messages import (
    BaseMessage,
    HumanMessage,
    SystemMessage,
    AIMessage,
    FunctionMessage,
)

from app.utils.config import settings
from app.utils.logger import log


# ── ReAct 子图状态 ──────────────────────────────────────────

class ReActState(TypedDict):
    messages: Annotated[List[BaseMessage], add_messages]
    iteration_count: int


# ── 工具 → OpenAI function schema 转换 ──────────────────────

def _tool_to_function(tool) -> dict:
    """将 LangChain @tool 转换为 OpenAI function calling schema"""
    schema = tool.args_schema.schema()
    return {
        "name": tool.name,
        "description": tool.description.split("\n")[0],  # 第一行作为简短描述
        "parameters": {
            "type": "object",
            "properties": schema.get("properties", {}),
            "required": schema.get("required", []),
        },
    }


# ── ReAct Agent 构建器 ──────────────────────────────────────

def build_react_agent(
    tools: List,
    system_prompt: str,
    max_iterations: int = 5,
    temperature: float = 0.3,
):
    """
    构建 ReAct Agent 子图

    Args:
        tools: LangChain @tool 工具列表
        system_prompt: 系统提示词（定义角色、工具使用策略、输出格式）
        max_iterations: 最大工具调用轮数（防止无限循环）
        temperature: LLM 温度

    Returns:
        编译后的 ReAct 子图（可直接 ainvoke）

    图结构:
        entry → agent → [has function_call?]
                         ├── yes (iter<max) → tools → agent (loop)
                         └── no  → END
    """

    function_schemas = [_tool_to_function(t) for t in tools]

    llm = ChatOpenAI(
        openai_api_key=settings.DASH_SCOPE_API_KEY,
        openai_api_base=settings.DASHSCOPE_BASE_URL,
        model=settings.QWEN_MODEL,
        temperature=temperature,
    )
    llm_with_functions = llm.bind_functions(functions=function_schemas)

    # ── 节点函数 ──────────────────────────────────────────

    def agent_node(state: ReActState) -> dict:
        """调用 LLM（绑定 functions），返回 AIMessage 可能含 function_call"""
        messages = state["messages"]
        iter_count = state.get("iteration_count", 0)

        # 首轮前面加上 system prompt
        if iter_count == 0:
            full_messages = [SystemMessage(content=system_prompt)] + list(messages)
        else:
            full_messages = list(messages)

        # 达到最大迭代前的最后一轮：强制终结提示
        if iter_count >= max_iterations - 1:
            full_messages = list(full_messages) + [
                SystemMessage(content=(
                    "这是最后一轮，你没有更多工具调用次数了。"
                    "请立即基于已有信息生成最终JSON结果回复患者，不要再调用任何工具。"
                    "即使某些信息不完整，也必须给出你的最佳判断。"
                ))
            ]

        log.info(f"[ReAct] agent 思考 - iteration: {iter_count + 1}")

        response = llm_with_functions.invoke(full_messages)

        fc = response.additional_kwargs.get("function_call") if response.additional_kwargs else None
        log.info(
            f"[ReAct] agent 响应 - "
            f"has_function_call: {fc is not None}, "
            f"content_preview: {(response.content or '')[:60]}"
        )

        return {
            "messages": [response],
            "iteration_count": iter_count + 1,
        }

    def tools_node(state: ReActState) -> dict:
        """执行 function_call 并返回 FunctionMessage"""
        messages = state["messages"]
        last_msg = messages[-1] if messages else None

        if not isinstance(last_msg, AIMessage):
            return {"messages": []}

        fc = last_msg.additional_kwargs.get("function_call") if last_msg.additional_kwargs else None
        if not fc:
            return {"messages": []}

        func_name = fc.get("name", "")
        func_args_raw = fc.get("arguments", "{}")

        # arguments 可能是 JSON 字符串
        try:
            func_args = json.loads(func_args_raw) if isinstance(func_args_raw, str) else func_args_raw
        except json.JSONDecodeError:
            func_args = {}

        log.info(f"[ReAct] 执行工具 - {func_name}({json.dumps(func_args, ensure_ascii=False)[:120]})")

        result = _execute_tool(tools, func_name, func_args)

        return {
            "messages": [
                FunctionMessage(
                    content=str(result)[:4000],
                    name=func_name,
                )
            ]
        }

    # ── 路由函数 ──────────────────────────────────────────

    def should_continue(state: ReActState) -> str:
        messages = state["messages"]
        iter_count = state.get("iteration_count", 0)

        if not messages:
            return "end"

        last_msg = messages[-1]

        if isinstance(last_msg, AIMessage):
            fc = last_msg.additional_kwargs.get("function_call") if last_msg.additional_kwargs else None
            if fc and fc.get("name"):
                if iter_count >= max_iterations:
                    log.warning(f"[ReAct] 达到最大迭代 {max_iterations}，强制结束")
                    return "end"
                return "tools"

        return "end"

    # ── 构建图 ──────────────────────────────────────────

    workflow = StateGraph(ReActState)
    workflow.add_node("agent", agent_node)
    workflow.add_node("tools", tools_node)

    workflow.set_entry_point("agent")
    workflow.add_conditional_edges(
        "agent",
        should_continue,
        {"tools": "tools", "end": END},
    )
    workflow.add_edge("tools", "agent")

    compiled = workflow.compile()
    log.info(f"[ReAct] 子图编译完成 - functions: {[f['name'] for f in function_schemas]}, max_iter: {max_iterations}")
    return compiled


# ── 工具执行辅助 ──────────────────────────────────────────

def _execute_tool(tools: List, tool_name: str, tool_args: Dict[str, Any]) -> str:
    """按名称查找并执行工具，返回结果字符串"""
    for tool in tools:
        if tool.name == tool_name:
            try:
                result = tool.invoke(tool_args)
                return str(result)
            except Exception as e:
                log.error(f"[ReAct] 工具执行失败 - {tool_name}: {str(e)}")
                return json.dumps({"error": str(e)})

    return json.dumps({"error": f"未找到工具: {tool_name}"})


# ── 便捷函数：从 ReAct 结果中提取最终回答 ─────────────────

def extract_final_response(result: dict) -> str:
    """从 ReAct 子图输出中提取最终 AI 回答文本"""
    messages = result.get("messages", [])
    if not messages:
        return ""
    last = messages[-1]
    if isinstance(last, AIMessage):
        return last.content or ""
    return ""


def parse_json_result(result: dict, field: str = None) -> dict:
    """
    从 ReAct 子图最终回答中解析 JSON 结果

    处理多种 LLM 输出格式：
    - 纯 JSON: {"key": "value"}
    - markdown 代码块: ```json\n{"key": "value"}\n```
    - 纯文本中夹杂 JSON（如 "思考：...\n{...}"）

    如果 LLM 返回的 JSON 缺少某些字段，自动补默认值。
    """
    parsed = {}
    text = extract_final_response(result)
    if not text:
        return parsed

    # 尝试 1: 去掉 markdown 代码块标记
    cleaned = text.strip()
    for pattern in [r'```(?:json)?\s*([\s\S]*?)```', r'```([\s\S]*?)```']:
        match = re.search(pattern, cleaned)
        if match:
            cleaned = match.group(1).strip()
            break

    # 尝试 2: 找到最外层的 { ... }
    for candidate in [cleaned, text]:
        s = candidate.find("{")
        e = candidate.rfind("}") + 1
        if s != -1 and e > s:
            try:
                data = json.loads(candidate[s:e])
                parsed = data if isinstance(data, dict) else parsed
                break
            except json.JSONDecodeError:
                continue

    # 自动补默认值 — LLM 可能遗漏字段
    defaults = {
        "recommended_department": "内科",
        "alternative_department": "",
        "confidence": 0.5,
        "urgency": "普通",
        "reasoning": "",
        "suggested_questions": [],
        "response_to_user": "",
        "summary": "",
        "abnormal_indicators": [],
        "recommendations": [],
    }
    for k, v in defaults.items():
        if k not in parsed or not parsed[k]:
            parsed[k] = v

    return parsed.get(field, parsed) if field else parsed
