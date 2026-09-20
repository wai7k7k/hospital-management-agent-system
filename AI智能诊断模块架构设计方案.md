# AI智能诊断模块 - LangGraph架构设计方案

## 📋 项目概述

**目标**：创建独立的AI智能诊断模块，采用 **Python + FastAPI + LangGraph** 技术栈，与现有Spring Cloud医院系统实现技术栈分离、业务深度融合。

**设计原则**：
- ✅ 架构分离：两套系统独立开发、独立部署、独立维护
- ✅ 业务融合：通过标准化接口实现深度业务集成
- ✅ 技术独立：AI模块使用Python生态，医院系统保持Java技术栈
- ✅ 数据互通：通过网关HTTP接口 + 消息队列实现双向数据流
- ✅ 状态管理：使用LangGraph实现复杂的多Agent协作和状态流转

---

## 🏗️ 整体架构设计

### 1. 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                     AI智能诊断模块 (Python)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  FastAPI     │  │  LangGraph   │  │  向量数据库   │          │
│  │  Web服务     │  │  工作流引擎   │  │  (Pinecone)  │          │
│  └──────┬───────┘  └──────┬───────┘  └──────────────┘          │
│         │                 │                                        │
│  ┌──────▼─────────────────▼──────────┐                          │
│  │     LangGraph工作流层              │                          │
│  │  ┌────────┐  ┌────────┐  ┌──────┐ │                          │
│  │  │意图识别│→ │智能分诊│→ │挂号  │ │                          │
│  │  │  节点  │  │  节点  │  │节点  │ │                          │
│  │  └───┬────┘  └───┬────┘  └──┬───┘ │                          │
│  │      │           │          │     │                          │
│  │  ┌───▼───────────▼──────────▼───┐ │                          │
│  │  │      报告解读节点             │ │                          │
│  │  └───┬───────────────────────────┘ │                          │
│  │      │                             │                          │
│  │  ┌───▼───────────┐                 │                          │
│  │  │  用药提醒节点  │                 │                          │
│  │  └───────────────┘                 │                          │
│  └─────────────────────────────────────┘                          │
│         │                                                          │
└─────────┼──────────────────────────────────────────────────────────┘
          │
          │ HTTP接口调用
          │
┌─────────▼──────────────────────────────────────────────────────────┐
│              Spring Cloud Gateway (8080)                           │
│         统一网关 - 路由转发 + 认证鉴权 + 限流熔断                    │
└─────────┬──────────────────────────────────────────────────────────┘
          │
    ┌─────┴─────┬──────────────┬──────────────┐
    │           │              │              │
┌───▼───┐  ┌───▼───┐     ┌───▼───┐      ┌───▼───┐
│ylxm-  │  │ylxm-  │     │ruoyi- │      │ruoyi- │
│reghos │  │customer│     │system │      │gateway│
│ 8071  │  │  8061  │     │       │      │       │
└───┬───┘  └───┬───┘     └───────┘      └───────┘
    │          │
    │    ┌─────▼─────────────────────────────────┐
    │    │       MySQL数据库                      │
    │    │  - patient (患者数据)                 │
    │    │  - treatment (治疗数据)               │
    │    └───────────────────────────────────────┘
    │
┌───▼──────────────────────────────────────────────────────────────┐
│                    消息队列 (RabbitMQ/RocketMQ)                   │
│         异步通知：检查报告、处方、用药信息实时同步                   │
└───────────────────────────────────────────────────────────────────┘
```

### 2. LangGraph工作流架构

```
                    ┌─────────────┐
                    │  用户输入    │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │  意图识别节点 │
                    │  (Intent)   │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
         ┌────▼────┐  ┌───▼────┐  ┌───▼────┐
         │ 智能分诊│  │医院信息│  │挂号预约│
         │ 节点    │  │查询节点│  │节点    │
         └────┬───┘  └───┬────┘  └───┬────┘
              │          │           │
              └──────────┼───────────┘
                         │
                    ┌────▼────┐
                    │ 报告解读│
                    │ 节点    │
                    └────┬────┘
                         │
                    ┌────▼────┐
                    │ 用药提醒│
                    │ 节点    │
                    └────┬────┘
                         │
                    ┌────▼────┐
                    │  响应生成│
                    │  节点    │
                    └────┬────┘
                         │
                    ┌────▼────┐
                    │  返回结果│
                    └─────────┘
```

---

## 🔧 技术栈设计

### AI智能诊断模块技术栈

| 组件 | 技术选型 | 版本 | 说明 |
|------|---------|------|------|
| **Web框架** | FastAPI | 0.104+ | 高性能异步Web框架 |
| **工作流引擎** | LangGraph | 0.2+ | 有状态多Agent工作流 |
| **大模型框架** | LangChain | 0.3+ | 大模型应用开发框架 |
| **大模型** | DeepSeek/通义千问 | - | 推理能力强，成本可控 |
| **向量数据库** | Pinecone | - | 高性能向量检索 |
| **对话记忆** | Redis | 7+ | 会话状态管理 |
| **图数据库** | PostgreSQL + pgvector | 15+ | 支持向量检索的关系数据库 |
| **消息队列** | RabbitMQ | 3.12+ | 异步消息通信 |
| **HTTP客户端** | httpx | 0.25+ | 异步HTTP请求 |
| **数据验证** | Pydantic | 2.5+ | 数据模型验证 |
| **状态管理** | LangGraph State | - | 工作流状态管理 |
| **API文档** | FastAPI自动生成 | - | Swagger UI |

### 医院系统现有技术栈

| 组件 | 技术选型 | 版本 |
|------|---------|------|
| **微服务框架** | Spring Cloud Alibaba | 2021.0.3 |
| **服务注册** | Nacos | 2.x |
| **网关** | Spring Cloud Gateway | 3.x |
| **服务调用** | Dubbo | 3.x |
| **数据库** | MySQL | 8.0 |
| **缓存** | Redis + Redisson | - |
| **分布式事务** | Seata | - |

---

## 🔄 LangGraph核心设计

### 1. 工作流状态定义

```python
from typing import TypedDict, Annotated, List, Optional
from langgraph.graph import add_messages

class DiagnosisState(TypedDict):
    """诊断工作流状态"""
    
    # 用户信息
    user_id: str
    session_id: str
    
    # 对话历史
    messages: Annotated[List, add_messages]
    
    # 意图识别结果
    intent: Optional[str]
    confidence: Optional[float]
    
    # 分诊相关
    symptoms: Optional[str]
    recommended_department: Optional[dict]
    
    # 挂号相关
    registration_info: Optional[dict]
    
    # 报告解读相关
    report_id: Optional[str]
    report_interpretation: Optional[str]
    
    # 用药提醒相关
    prescription_id: Optional[str]
    medication_reminders: Optional[List[dict]]
    
    # 最终响应
    response: Optional[str]
    
    # 错误信息
    error: Optional[str]
```

### 2. LangGraph节点定义

```python
from langgraph.graph import StateGraph, END

# 1. 意图识别节点
async def intent_recognition_node(state: DiagnosisState) -> DiagnosisState:
    """识别用户意图：分诊/挂号/报告解读/用药提醒"""
    pass

# 2. 智能分诊节点
async def triage_node(state: DiagnosisState) -> DiagnosisState:
    """基于症状推荐科室"""
    pass

# 3. 医院信息查询节点
async def hospital_info_node(state: DiagnosisState) -> DiagnosisState:
    """查询科室、医生、排班等信息"""
    pass

# 4. 挂号预约节点
async def registration_node(state: DiagnosisState) -> DiagnosisState:
    """协助用户完成挂号"""
    pass

# 5. 报告解读节点
async def report_interpretation_node(state: DiagnosisState) -> DiagnosisState:
    """AI解读检查报告"""
    pass

# 6. 用药提醒节点
async def medication_reminder_node(state: DiagnosisState) -> DiagnosisState:
    """生成用药计划和提醒"""
    pass

# 7. 响应生成节点
async def response_generation_node(state: DiagnosisState) -> DiagnosisState:
    """生成最终响应"""
    pass
```

### 3. LangGraph边定义

```python
from langgraph.graph import StateGraph, END

# 条件边：根据意图路由到不同节点
def route_by_intent(state: DiagnosisState) -> str:
    """根据意图路由到不同节点"""
    intent = state.get("intent")
    
    if intent == "triage":
        return "triage_node"
    elif intent == "registration":
        return "registration_node"
    elif intent == "report_interpretation":
        return "report_interpretation_node"
    elif intent == "medication_reminder":
        return "medication_reminder_node"
    elif intent == "hospital_info":
        return "hospital_info_node"
    else:
        return "response_generation_node"

# 构建工作流图
def build_diagnosis_graph():
    """构建诊断工作流图"""
    
    workflow = StateGraph(DiagnosisState)
    
    # 添加节点
    workflow.add_node("intent_recognition", intent_recognition_node)
    workflow.add_node("triage", triage_node)
    workflow.add_node("hospital_info", hospital_info_node)
    workflow.add_node("registration", registration_node)
    workflow.add_node("report_interpretation", report_interpretation_node)
    workflow.add_node("medication_reminder", medication_reminder_node)
    workflow.add_node("response_generation", response_generation_node)
    
    # 设置入口节点
    workflow.set_entry_point("intent_recognition")
    
    # 添加条件边
    workflow.add_conditional_edges(
        "intent_recognition",
        route_by_intent,
        {
            "triage_node": "triage",
            "hospital_info_node": "hospital_info",
            "registration_node": "registration",
            "report_interpretation_node": "report_interpretation",
            "medication_reminder_node": "medication_reminder",
            "response_generation_node": "response_generation"
        }
    )
    
    # 添加边到响应生成节点
    workflow.add_edge("triage", "response_generation")
    workflow.add_edge("hospital_info", "response_generation")
    workflow.add_edge("registration", "response_generation")
    workflow.add_edge("report_interpretation", "response_generation")
    workflow.add_edge("medication_reminder", "response_generation")
    
    # 设置结束节点
    workflow.add_edge("response_generation", END)
    
    # 编译工作流
    return workflow.compile()
```

### 4. LangGraph可视化

```python
# 生成工作流图的可视化
graph = build_diagnosis_graph()

# 保存为PNG图片
graph.get_graph().print_ascii()
graph.get_graph().draw_mermaid_png(output_file_path="diagnosis_graph.png")
```

---

## 📡 系统互通方式设计

### 1. 网关HTTP接口（AI → 医院系统）

AI模块通过Gateway调用医院系统提供的RESTful API：

#### 1.1 患者信息查询
```http
GET /api/patient/{patientId}
Authorization: Bearer {token}
```

#### 1.2 症状智能分诊
```http
POST /api/ai/triage
Content-Type: application/json

{
  "symptoms": "头痛、恶心、视力模糊",
  "patientId": "12345"
}
```

#### 1.3 科室与值班医生查询
```http
GET /api/department/{deptId}/doctors?date=2024-01-15
```

#### 1.4 一键挂号
```http
POST /api/register
Content-Type: application/json

{
  "patientId": "12345",
  "deptId": "DEPT001",
  "doctorId": "DOC001",
  "appointmentTime": "2024-01-15 09:00"
}
```

#### 1.5 检查结果获取
```http
GET /api/inspect/result/{orderId}
```

#### 1.6 处方用药查询
```http
GET /api/prescription/{prescriptionId}
```

### 2. 消息队列（医院系统 → AI模块）

医院系统通过消息队列异步推送数据到AI模块：

#### 2.1 检查报告生成通知
```json
{
  "eventType": "INSPECT_REPORT_GENERATED",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "reportId": "RPT001",
    "patientId": "12345",
    "inspectType": "血常规",
    "resultUrl": "https://hospital.com/report/RPT001.pdf",
    "abnormalItems": ["白细胞计数偏高", "血红蛋白偏低"]
  }
}
```

#### 2.2 处方开具通知
```json
{
  "eventType": "PRESCRIPTION_CREATED",
  "timestamp": "2024-01-15T11:00:00Z",
  "data": {
    "prescriptionId": "PRE001",
    "patientId": "12345",
    "medicines": [
      {
        "medicineName": "阿莫西林",
        "dosage": "0.5g",
        "frequency": "每日3次",
        "duration": "7天"
      }
    ],
    "doctorAdvice": "饭后服用，忌酒"
  }
}
```

#### 2.3 用药提醒触发
```json
{
  "eventType": "MEDICATION_REMINDER",
  "timestamp": "2024-01-15T08:00:00Z",
  "data": {
    "patientId": "12345",
    "prescriptionId": "PRE001",
    "medicineName": "阿莫西林",
    "scheduledTime": "2024-01-15T08:00:00Z"
  }
}
```

---

## 🔄 业务闭环流程设计

### 完整患者就医流程（LangGraph工作流）

```
┌─────────────┐
│  患者登录   │
└──────┬──────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤1：症状咨询                                             │
│  患者描述症状 → LangGraph意图识别 → 智能分诊节点             │
│  调用：POST /api/v1/chat                                     │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤2：智能分诊（LangGraph节点）                            │
│  triage_node分析症状 → 调用医院API查询科室 → 推荐结果        │
│  状态流转：intent → triage → hospital_info → response       │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤3：挂号预约（LangGraph节点）                            │
│  registration_node → 调用医院API → 完成挂号                  │
│  状态流转：intent → registration → response                 │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤4：线下就诊                                             │
│  患者到医院就诊 → 医生开具检查单/处方                        │
│  医院系统处理业务                                            │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤5：检查报告生成                                         │
│  检查完成 → 报告生成 → 消息队列通知AI模块                     │
│  事件：INSPECT_REPORT_GENERATED                              │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤6：AI报告解读（LangGraph节点）                          │
│  report_interpretation_node → AI解读 → 生成说明             │
│  推送解读结果给患者                                          │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤7：处方用药提醒（LangGraph节点）                        │
│  medication_reminder_node → 生成用药计划 → 定时提醒          │
│  事件：PRESCRIPTION_CREATED, MEDICATION_REMINDER            │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  步骤8：随访关怀                                             │
│  LangGraph工作流 → 定期随访 → 健康建议                      │
└──────┬──────────────────────────────────────────────────────┘
       │
┌──────▼──────────────────────────────────────────────────────┐
│  流程闭环完成                                                │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 AI模块项目结构

```
ai-diagnosis-system/
├── app/
│   ├── __init__.py
│   ├── main.py                 # FastAPI应用入口
│   ├── config.py               # 配置管理
│   ├── dependencies.py         # 依赖注入
│   │
│   ├── api/                    # API路由层
│   │   ├── __init__.py
│   │   ├── v1/
│   │   │   ├── __init__.py
│   │   │   ├── chat.py         # 对话接口
│   │   │   ├── triage.py       # 智能分诊
│   │   │   ├── register.py     # 挂号辅助
│   │   │   ├── report.py       # 报告解读
│   │   │   ├── medication.py   # 用药提醒
│   │   │   └── patient.py      # 患者管理
│   │   └── dependencies.py     # API依赖
│   │
│   ├── core/                   # 核心业务层
│   │   ├── __init__.py
│   │   │
│   │   ├── graph/              # LangGraph工作流
│   │   │   ├── __init__.py
│   │   │   ├── state.py        # 状态定义
│   │   │   ├── nodes.py        # 节点定义
│   │   │   ├── edges.py        # 边定义
│   │   │   ├── builder.py      # 工作流构建器
│   │   │   └── visualizer.py   # 可视化工具
│   │   │
│   │   ├── agents/             # LangChain Agents
│   │   │   ├── __init__.py
│   │   │   ├── base_agent.py   # 基础Agent
│   │   │   ├── triage_agent.py # 分诊Agent
│   │   │   ├── report_agent.py # 报告解读Agent
│   │   │   ├── medication_agent.py  # 用药Agent
│   │   │   └── hospital_agent.py    # 医院信息Agent
│   │   │
│   │   ├── tools/              # 工具函数
│   │   │   ├── __init__.py
│   │   │   ├── hospital_api.py # 医院系统API调用
│   │   │   ├── knowledge_retriever.py  # 知识检索
│   │   │   ├── reminder.py     # 提醒工具
│   │   │   └── vector_search.py    # 向量搜索
│   │   │
│   │   ├── prompts/            # 提示词模板
│   │   │   ├── intent_prompt.txt
│   │   │   ├── triage_prompt.txt
│   │   │   ├── report_prompt.txt
│   │   │   ├── medication_prompt.txt
│   │   │   └── hospital_prompt.txt
│   │   │
│   │   └── memory/             # 对话记忆
│   │       ├── __init__.py
│   │       ├── redis_memory.py # Redis记忆存储
│   │       └── memory_manager.py
│   │
│   ├── models/                 # 数据模型
│   │   ├── __init__.py
│   │   ├── schemas.py          # Pydantic模型
│   │   ├── database.py         # 数据库连接
│   │   └── entities.py         # ORM实体
│   │
│   ├── services/               # 业务服务层
│   │   ├── __init__.py
│   │   ├── graph_service.py    # 工作流服务
│   │   ├── triage_service.py   # 分诊服务
│   │   ├── register_service.py # 挂号服务
│   │   ├── report_service.py   # 报告服务
│   │   ├── medication_service.py  # 用药服务
│   │   └── patient_service.py  # 患者服务
│   │
│   ├── integrations/           # 外部集成
│   │   ├── __init__.py
│   │   ├── hospital_client.py  # 医院系统HTTP客户端
│   │   ├── message_queue.py    # 消息队列消费者
│   │   └── vector_store.py     # 向量数据库
│   │
│   └── utils/                  # 工具类
│       ├── __init__.py
│       ├── logger.py
│       ├── exceptions.py
│       └── helpers.py
│
├── tests/                      # 测试
│   ├── __init__.py
│   ├── test_graph/
│   │   ├── test_nodes.py
│   │   ├── test_edges.py
│   │   └── test_workflow.py
│   ├── test_agents/
│   ├── test_services/
│   └── test_api/
│
├── scripts/                    # 脚本
│   ├── init_knowledge.py       # 初始化知识库
│   ├── test_graph.py           # 测试工作流
│   ├── visualize_graph.py      # 可视化工作流
│   └── deploy.sh               # 部署脚本
│
├── docs/                       # 文档
│   ├── API.md                  # API文档
│   ├── ARCHITECTURE.md         # 架构文档
│   ├── LANGGRAPH.md            # LangGraph使用文档
│   └── DEPLOYMENT.md           # 部署文档
│
├── requirements.txt            # Python依赖
├── Dockerfile                  # Docker镜像
├── docker-compose.yml          # Docker编排
├── .env.example                # 环境变量示例
├── .gitignore
└── README.md
```

---

## 🔌 接口规范设计

### AI模块对外接口（FastAPI）

#### 1. 智能对话接口（LangGraph工作流）
```python
POST /api/v1/chat
Content-Type: application/json

{
  "session_id": "sess_12345",
  "message": "我最近头痛，应该挂什么科？",
  "patient_id": "12345"
}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "reply": "根据您的症状描述，建议您挂神经内科。头痛可能由多种原因引起...",
    "intent": "triage",
    "workflow_id": "wf_abc123",
    "state": {
      "intent": "triage",
      "confidence": 0.95,
      "recommended_department": {
        "id": "DEPT001",
        "name": "神经内科"
      }
    },
    "suggestions": [
      {
        "type": "department",
        "name": "神经内科",
        "id": "DEPT001"
      }
    ]
  }
}
```

#### 2. 智能分诊接口
```python
POST /api/v1/triage
Content-Type: application/json

{
  "symptoms": "头痛、恶心、视力模糊",
  "patient_id": "12345",
  "duration": "3天"
}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "recommended_department": {
      "id": "DEPT001",
      "name": "神经内科",
      "reason": "症状符合神经系统疾病特征"
    },
    "urgency": "medium",
    "advice": "建议尽快就诊，避免延误病情",
    "workflow_id": "wf_def456"
  }
}
```

#### 3. 报告解读接口
```python
POST /api/v1/report/interpret
Content-Type: application/json

{
  "report_id": "RPT001",
  "patient_id": "12345",
  "report_type": "血常规"
}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "summary": "您的血常规检查结果显示白细胞计数略高，提示可能有轻微炎症...",
    "abnormal_items": [
      {
        "item": "白细胞计数",
        "value": "11.2×10⁹/L",
        "reference": "4-10×10⁹/L",
        "interpretation": "轻度升高，可能提示细菌感染"
      }
    ],
    "recommendations": [
      "建议结合临床症状进一步检查",
      "注意休息，多饮水"
    ],
    "workflow_id": "wf_ghi789"
  }
}
```

#### 4. 用药提醒接口
```python
POST /api/v1/medication/reminder
Content-Type: application/json

{
  "prescription_id": "PRE001",
  "patient_id": "12345"
}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "medicines": [
      {
        "name": "阿莫西林",
        "next_dose_time": "2024-01-15T14:00:00Z",
        "instructions": "饭后服用，每次0.5g，每日3次"
      }
    ],
    "reminders_enabled": true,
    "workflow_id": "wf_jkl012"
  }
}
```

#### 5. 工作流状态查询接口
```python
GET /api/v1/workflow/{workflow_id}

Response:
{
  "code": 200,
  "message": "success",
  "data": {
    "workflow_id": "wf_abc123",
    "status": "completed",
    "current_node": "response_generation",
    "state": {
      "intent": "triage",
      "symptoms": "头痛、恶心",
      "recommended_department": {
        "id": "DEPT001",
        "name": "神经内科"
      },
      "response": "根据您的症状..."
    },
    "execution_history": [
      {
        "node": "intent_recognition",
        "timestamp": "2024-01-15T10:00:00Z",
        "duration_ms": 150
      },
      {
        "node": "triage",
        "timestamp": "2024-01-15T10:00:00Z",
        "duration_ms": 2000
      }
    ]
  }
}
```

---

## 🗄️ 数据库设计

### AI模块PostgreSQL数据库

```sql
-- 启用pgvector扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 会话表
CREATE TABLE chat_sessions (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(64) UNIQUE NOT NULL,
    patient_id VARCHAR(64),
    user_id VARCHAR(64),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active'
);

-- 消息表
CREATE TABLE chat_messages (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(64) REFERENCES chat_sessions(session_id),
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tokens INTEGER
);

-- 工作流执行记录表
CREATE TABLE workflow_executions (
    id SERIAL PRIMARY KEY,
    workflow_id VARCHAR(64) UNIQUE NOT NULL,
    session_id VARCHAR(64) REFERENCES chat_sessions(session_id),
    patient_id VARCHAR(64),
    status VARCHAR(20) DEFAULT 'running',
    current_node VARCHAR(100),
    state JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- 工作流节点执行历史表
CREATE TABLE workflow_node_history (
    id SERIAL PRIMARY KEY,
    workflow_id VARCHAR(64) REFERENCES workflow_executions(workflow_id),
    node_name VARCHAR(100) NOT NULL,
    input_state JSONB,
    output_state JSONB,
    duration_ms INTEGER,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分诊记录表
CREATE TABLE triage_records (
    id SERIAL PRIMARY KEY,
    patient_id VARCHAR(64) NOT NULL,
    symptoms TEXT NOT NULL,
    recommended_dept_id VARCHAR(64),
    recommended_dept_name VARCHAR(100),
    confidence DECIMAL(3,2),
    urgency VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 报告解读记录表
CREATE TABLE report_interpretations (
    id SERIAL PRIMARY KEY,
    report_id VARCHAR(64) UNIQUE NOT NULL,
    patient_id VARCHAR(64) NOT NULL,
    report_type VARCHAR(50),
    interpretation TEXT,
    abnormal_items JSONB,
    recommendations TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用药提醒表
CREATE TABLE medication_reminders (
    id SERIAL PRIMARY KEY,
    patient_id VARCHAR(64) NOT NULL,
    prescription_id VARCHAR(64) NOT NULL,
    medicine_name VARCHAR(100) NOT NULL,
    reminder_time TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 知识库文档表
CREATE TABLE knowledge_documents (
    id SERIAL PRIMARY KEY,
    doc_id VARCHAR(64) UNIQUE NOT NULL,
    title VARCHAR(200),
    content TEXT,
    category VARCHAR(50),
    embedding vector(1536),
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_workflow_executions_session_id ON workflow_executions(session_id);
CREATE INDEX idx_workflow_executions_status ON workflow_executions(status);
CREATE INDEX idx_workflow_node_history_workflow_id ON workflow_node_history(workflow_id);
CREATE INDEX idx_knowledge_documents_category ON knowledge_documents(category);
CREATE INDEX idx_knowledge_documents_embedding ON knowledge_documents USING ivfflat (embedding vector_cosine_ops);
```

---

## 🚀 部署架构

### Docker Compose部署

```yaml
version: '3.8'

services:
  # AI诊断服务
  ai-diagnosis:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://user:pass@postgres:5432/ai_diagnosis
      - REDIS_URL=redis://redis:6379
      - RABBITMQ_URL=amqp://user:pass@rabbitmq:5672
      - LANGCHAIN_API_KEY=${LANGCHAIN_API_KEY}
      - PINECONE_API_KEY=${PINECONE_API_KEY}
    depends_on:
      - postgres
      - redis
      - rabbitmq
    volumes:
      - ./logs:/app/logs

  # PostgreSQL数据库
  postgres:
    image: pgvector/pgvector:pg15
    environment:
      - POSTGRES_DB=ai_diagnosis
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  # Redis缓存
  redis:
    image: redis:7-alpine
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"

  # RabbitMQ消息队列
  rabbitmq:
    image: rabbitmq:3.12-management
    environment:
      - RABBITMQ_DEFAULT_USER=user
      - RABBITMQ_DEFAULT_PASS=pass
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

  # LangSmith（可选，用于LangChain调试）
  langsmith:
    image: langchain/langsmith
    environment:
      - LANGCHAIN_API_KEY=${LANGCHAIN_API_KEY}
      - LANGCHAIN_TRACING_V2=true
    ports:
      - "1984:1984"

volumes:
  postgres_data:
  redis_data:
  rabbitmq_data:
```

---

## 🔐 安全设计

### 1. 认证鉴权
- AI模块与医院系统通过JWT Token进行认证
- Gateway统一鉴权，AI模块验证Token有效性

### 2. 数据加密
- 敏感数据传输使用HTTPS
- 数据库连接使用SSL
- 患者隐私数据加密存储

### 3. 访问控制
- 基于角色的访问控制（RBAC）
- API限流防刷
- 敏感操作审计日志

### 4. LangGraph安全
- 工作流执行权限控制
- 状态数据加密存储
- 节点执行超时限制

---

## 📊 监控告警

### 1. 应用监控
- Prometheus + Grafana监控
- 关键指标：QPS、响应时间、错误率、工作流执行时间

### 2. LangGraph监控
- 工作流执行状态监控
- 节点执行时间统计
- 状态流转追踪

### 3. 日志收集
- ELK Stack日志分析
- 结构化日志输出
- 工作流执行日志

### 4. 告警机制
- 异常告警（钉钉/企业微信）
- 服务健康检查
- 工作流执行失败告警

---

## 🎯 实施计划

### Phase 1: 基础框架搭建（2周）
- [x] FastAPI项目初始化
- [x] PostgreSQL + pgvector数据库设计
- [x] LangGraph基础框架搭建
- [x] 状态定义和节点框架
- [x] Docker环境搭建

### Phase 2: LangGraph工作流开发（4周）
- [ ] 意图识别节点开发
- [ ] 智能分诊节点开发
- [ ] 医院信息查询节点开发
- [ ] 挂号预约节点开发
- [ ] 报告解读节点开发
- [ ] 用药提醒节点开发
- [ ] 响应生成节点开发
- [ ] 工作流边和路由逻辑

### Phase 3: LangChain Agents集成（2周）
- [ ] 分诊Agent开发
- [ ] 报告解读Agent开发
- [ ] 用药提醒Agent开发
- [ ] 医院信息Agent开发
- [ ] 工具函数集成

### Phase 4: 外部系统集成（2周）
- [ ] 医院系统HTTP客户端开发
- [ ] RabbitMQ消费者开发
- [ ] 异步事件处理
- [ ] 消息可靠性保证

### Phase 5: 测试与优化（2周）
- [ ] 单元测试
- [ ] 工作流集成测试
- [ ] 性能优化
- [ ] 安全加固

### Phase 6: 部署上线（1周）
- [ ] 生产环境部署
- [ ] 监控告警配置
- [ ] 文档完善

---

## 📝 LangGraph优势

### 为什么选择LangGraph？

1. **有状态工作流**
   - 支持复杂的多步骤业务流程
   - 状态自动管理和传递
   - 支持循环和条件分支

2. **可视化调试**
   - 工作流图可视化
   - 节点执行追踪
   - 状态流转监控

3. **灵活的路由**
   - 基于条件的动态路由
   - 支持并行执行
   - 易于扩展新节点

4. **与LangChain深度集成**
   - 无缝使用LangChain工具
   - 支持多种大模型
   - 丰富的生态系统

5. **生产就绪**
   - 异常处理机制
   - 重试和回滚
   - 持久化支持

---

## 📝 总结

本设计方案实现了：

✅ **架构分离**：Python AI模块与Java医院系统完全独立  
✅ **业务融合**：通过HTTP接口和消息队列实现深度集成  
✅ **技术独立**：各自使用最适合的技术栈  
✅ **数据互通**：双向数据流，实时同步  
✅ **可扩展性**：LangGraph模块化设计，易于扩展新功能  
✅ **高可用性**：容器化部署，支持水平扩展  
✅ **状态管理**：LangGraph提供强大的工作流状态管理  
✅ **可视化**：工作流图可视化，便于调试和维护  

该方案充分利用了 **LangGraph** 的优势，为医疗诊断场景提供了完美的解决方案！
