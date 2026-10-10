---
name: hospital-ai-service
description: AI 智能诊断服务 —— 基于 FastAPI + LangGraph + 通义千问的医疗 AI 助手
metadata:
  type: reference
---

# hospital-ai-service (AI 智能诊断服务)

## 概述

医院智能 AI 服务 —— 基于 LangGraph 的医疗助手，提供智能分诊、挂号、报告解读、用药咨询等功能。

## 技术栈

| 维度 | 技术 |
|------|------|
| 语言 | Python 3.10+ |
| 框架 | FastAPI |
| AI 编排 | LangGraph + LangChain |
| LLM | 通义千问 (qwen-plus) |
| 向量库 | Pinecone |
| 数据库 | MongoDB (对话记忆 + 挂号状态) |
| 消息队列 | RabbitMQ (事件驱动) |
| 监控 | Prometheus |

## 核心文件

| 文件 | 职责 |
|------|------|
| `app/main.py` | FastAPI 应用入口，生命周期管理，路由注册 |
| `app/api/v1/chat.py` | SSE 流式聊天 + REST 聊天 + 历史管理 |
| `app/api/v1/websocket.py` | WebSocket 实时聊天 |
| `app/api/v1/knowledge.py` | 知识库管理 API |
| `app/core/graph/workflow.py` | LangGraph 工作流管理器 |
| `app/core/graph/nodes.py` | 工作流节点定义（意图识别→分诊→挂号→报告→用药→响应生成） |
| `app/core/graph/react_agent.py` | ReAct Agent 构建器 |
| `app/core/services/intent_service.py` | 中央意图识别（IntentService） |
| `app/core/services/register_service.py` | 挂号服务编排 |
| `app/core/services/triage_service.py` | 分诊服务 |
| `app/core/services/report_service.py` | 报告解读服务 |
| `app/core/services/medication_service.py` | 用药安全服务 |
| `app/core/agents/register_agent.py` | 挂号 Agent（多轮对话状态机） |
| `app/core/agents/triage_agent.py` | 分诊 Agent（ReAct 模式） |
| `app/core/agents/report_agent.py` | 报告解读 Agent |
| `app/core/agents/medication_agent.py` | 用药 Agent |
| `app/core/agents/hospital_agent.py` | 医院信息 Agent |
| `app/core/knowledge/` | 知识库（文档加载/分块/向量化/检索） |
| `app/core/events/` | 事件驱动（检查/报告/处方事件） |
| `app/core/memory/chat_memory.py` | Agent 记忆隔离 |
| `app/integrations/hospital/http_client.py` | HIS API 客户端 |

## 数据流

```
患者消息 → IntentService.recognize_intent()
         → LangGraph Workflow
           → intent_recognition (意图识别)
           → route_intent (路由到专业节点)
             ├─ triage (ReAct 分诊)
             ├─ register (多轮挂号)
             ├─ report (报告解读)
             ├─ medication (用药检查)
             ├─ hospital_info (医院查询)
             └─ response_generation (响应生成)
         → SSE 流式返回给前端
```

## LangGraph 状态模型（GraphState）

定义在 `app/core/graph/nodes.py:9-26`。它是一张**会诊流转单**：所有节点读写同一份，每个节点领到后填几栏再传给下一个；最终患者看到的是 `response` 那一栏。

### 字段分组

> 类型中 `Optional[...]` 只是写给类型检查器看的，运行时该 key 不存在就是不存在，必须靠 `chat.py:41-50` 的 `initial_state` 显式初始化。

**① 身份标识（谁在问）**

| 字段 | 说明 |
|------|------|
| `session_id: str` | 会话 ID。**唯一无 `Optional` 的必填字段**，全链路靠它取 MongoDB 历史对话与挂号状态 |
| `user_id: Optional[str]` | 登录用户 ID |
| `patient_id: Optional[str]` | 患者 ID。与 `user_id` 分开，因为存在代人问诊 |

**② 输入（问了什么）**

| 字段 | 说明 |
|------|------|
| `messages: List[ChatMessage]` | 聊天消息列表 |

`ChatMessage`（`request.py:17-21`）= `role`(user/assistant/system) + `content` + `timestamp`。

节点基本只取**最后一条**作为本次提问，其余作为 LLM 上下文（`nodes.py:37-38`）：

```python
last_message = state['messages'][-1].content if state['messages'] else ""
history = state['messages'][:-1] if len(state['messages']) > 1 else []
```

**③ 路由决策（走哪条路）**

| 字段 | 说明 |
|------|------|
| `intent: Optional[IntentType]` | 意图，6 选 1（见下） |
| `confidence: Optional[float]` | 置信度 0~1 |

`IntentType`（`request.py:7-14`）：`TRIAGE` / `REGISTER` / `REPORT` / `MEDICATION` / `HOSPITAL` / `CHAT`。这两个字段是 `route_intent` 的唯一输入。

`confidence` 还兼作**挂号会话的跳出阈值**——用户聊别的只有置信度 > 0.6 才允许跳出（`nodes.py:47`）：

```python
if has_active and llm_intent != IntentType.REGISTER and llm_confidence > 0.6:
```

**④ 业务结果（各节点交的作业）**

| 字段 | 产出节点 |
|------|----------|
| `triage_result: Optional[dict]` | `triage_node`（推荐科室、紧急度、是否需追问） |
| `register_result: Optional[dict]` | `register_node` |
| `report_result: Optional[dict]` | `report_node`（异常指标） |
| `medication_result: Optional[dict]` | `medication_node`（药物冲突） |
| `symptoms: Optional[str]` | `triage_node`（**见「已知问题」） |

**一次请求只会填其中 1 个，其余全程为 `None`** —— 因为父图是星型结构，9 个分支只走 1 条。这些字段本质是**预留的槽位**。

**⑤ 事件驱动入口（不是人在问，是系统在触发）**

| 字段 | 说明 |
|------|------|
| `event_type: Optional[str]` | `examination_ordered` / `report_generated` / `prescription_created` |
| `event_data: Optional[dict]` | 事件完整数据 |

HIS 开检查单/出报告/开处方时经 RabbitMQ 推送，走**独立于用户对话的入口**。在 `route_intent` 中优先级**高于** `intent`（`nodes.py:534-539`），即事件请求会绕过意图分支直接进 3 个 event 节点。

**⑥ 输出与异常**

| 字段 | 说明 |
|------|------|
| `response: Optional[str]` | 最终回复患者的文本，全图唯一出口 |
| `error: Optional[str]` | 异常信息。**不会中断流程**，只记录，后续节点照常执行 |
| `metadata: dict` | 杂项兜底袋 |

`metadata` 实际装载内容：来自 `IntentService` 的 `reasoning`（LLM 推理过程）、`agent`、`extracted_info`、`raw_response`（`intent_service.py:103-109`），以及 `nodes.py:53` 追加的 `register_session_active`。

### TypedDict 的三个要点

1. **运行时就是普通 dict，不做任何类型校验**。`state["confidence"] = "很高"` 不会报错。
2. **不设默认值**。`Optional[...]` 仅对静态类型检查器有意义。
3. **所有字段均无 reducer**（对比 `react_agent.py:40` 的 `Annotated[List[BaseMessage], add_messages]`）。默认覆盖语义，因此父图节点可以 `return state` 全量返回——语义正确，但绕过了「只提交增量」的框架设计。

## 已知问题与技术债

| # | 问题 | 位置 | 影响 | 建议 |
|---|------|------|------|------|
| 1 | `symptoms` **只写不读** | 写入：`nodes.py:161` | 全仓无任何 `state['symptoms']` / `state.get('symptoms')` 读取点。且写入的是**整句原文**（`"我头疼三天了"`）而非结构化症状 | 要么删除，要么改由 `triage_result` 内的结构化症状填充 |
| 2 | `DiagnosisState` 死代码 | 定义 `request.py:98`<br>导入 `nodes.py:4` | 全仓仅此 2 处命中，**无任何使用**。Pydantic `BaseModel`，字段与 `GraphState` 几乎重复 | 可安全删除（含 `nodes.py:4` 的 import） |
| 3 | `metadata` 必须用 `.update()` | `nodes.py:52` | 若写成 `state['metadata'] = {...}` 直接赋值，会冲掉前面节点塞入的 `register_session_active` | 保持现状，改动时留意 |

**问题 2 的历史成因**：`AI智能诊断模块架构设计方案.md:157` 中的状态定义恰恰名为 `DiagnosisState(TypedDict)`。可推断早期设计稿用此名，实现时改名为 `GraphState`，而 `request.py` 中的 Pydantic 版本是**改名过程的残留**。

## 路由设计

| 路由 | 方法 | 说明 |
|------|------|------|
| `/v1/chat/stream` | POST | SSE 流式聊天 |
| `/v1/chat/` | POST | REST 聊天 |
| `/v1/chat/history/{session_id}` | GET | 获取历史 |
| `/v1/chat/{session_id}` | DELETE | 清除历史 |
| `/v1/ws/chat/{session_id}` | WS | WebSocket 聊天 |

## 配置要点

* 端口：8000
* 通过 `/api/ai/` 前缀从 Nginx 代理访问
* LLM API Key: `DASH_SCOPE_API_KEY`
* HIS API 基地址: `HOSPITAL_API_BASE_URL` (默认 localhost:8071)

## 与其他模块的关系

| 关联模块 | 关系类型 | 说明 |
|----------|----------|------|
| `ylxm-reghos` | HTTP API | 调用挂号、科室接口 |
| `ylxm-customer` | HTTP API | 查询患者信息 |
| 前端 UI | SSE/HTTP | 提供聊天接口 |

## 变更记录

| 日期 | 变更内容 | 关联 |
|------|----------|------|
| 2026-06-08 | 初始文档 | - |
| 2026-09-02 | 补充「LangGraph 状态模型（GraphState）」字段级说明、「已知问题与技术债」3 项 | [[learning/2026-09-02]] |
