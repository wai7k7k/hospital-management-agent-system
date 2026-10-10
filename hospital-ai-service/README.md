# 医院智能 AI 服务

基于 LangGraph 的医疗助手，使用 FastAPI、通义千问、Pinecone、RabbitMQ 和 MongoDB 构建。

## 技术栈

- **FastAPI**: 高性能 Python Web 框架
- **LangGraph**: AI 工作流编排
- **通义千问 (Qwen)**: 大语言模型
- **Pinecone**: 向量数据库
- **RabbitMQ**: 消息队列
- **MongoDB**: 文档数据库
- **WebSocket**: 实时通信
- **SSE**: 服务器推送事件

## 功能特性

- 智能分诊：根据症状推荐科室
- 在线挂号：自动处理挂号请求
- 报告解读：AI 解读医疗报告
- 用药提醒：检查药物相互作用
- 实时聊天：WebSocket 实时对话
- 消息推送：SSE 事件通知

## 快速开始

### 1. 环境准备

```bash
# 克隆项目
cd hospital-ai-service

# 创建虚拟环境
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 安装依赖
pip install -r requirements.txt
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env .env

# 编辑 .env 文件，填入你的配置
# 必须配置：
# - DASH_SCOPE_API_KEY: 通义千问 API 密钥
# - PINECONE_API_KEY: Pinecone API 密钥
```

### 3. 启动服务

```bash
# 使用 Docker Compose（推荐）
docker-compose up -d

# 或直接运行
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 4. 访问服务

- API 文档: http://localhost:8000/docs
- 健康检查: http://localhost:8000/health
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3001

## API 使用示例

### 聊天接口

```bash
curl -X POST "http://localhost:8000/v1/chat/" \
  -H "Content-Type: application/json" \
  -d '{
    "session_id": "test-session-001",
    "message": "我头痛，应该挂哪个科？",
    "user_id": "user-001"
  }'
```

### WebSocket 聊天

```javascript
const ws = new WebSocket('ws://localhost:8000/v1/ws/chat/test-session-001');

ws.onopen = () => {
  console.log('WebSocket 连接已建立');
  
  // 发送消息
  ws.send(JSON.stringify({
    type: 'chat',
    content: '我头痛，应该挂哪个科？',
    user_id: 'user-001'
  }));
};

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  console.log('收到消息:', message);
};
```

## 项目结构

```
hospital-ai-service/
├── app/
│   ├── api/              # API 接口层
│   │   └── v1/
│   │       ├── chat.py   # 聊天接口
│   │       └── websocket.py  # WebSocket 接口
│   ├── core/             # 核心业务层
│   │   ├── graph/        # LangGraph 工作流
│   │   ├── models/       # 数据模型
│   │   └── services/     # 业务服务
│   ├── integrations/     # 外部集成
│   │   ├── hospital/     # 医院系统集成
│   │   ├── mq/           # 消息队列
│   │   └── vector/       # 向量数据库
│   ├── utils/            # 工具类
│   └── main.py           # 应用入口
├── docker/               # Docker 配置
├── tests/                # 测试代码
├── docs/                 # 文档
├── logs/                 # 日志文件
├── Dockerfile
├── docker-compose.yml
├── requirements.txt
├── pyproject.toml
└── .env.example
```

## 开发指南

### 添加新的意图类型

1. 在 `app/core/models/request.py` 中添加新的 IntentType
2. 在 `app/core/graph/nodes.py` 中添加对应的处理节点
3. 在 `app/core/services/intent_service.py` 中添加响应生成逻辑

### 集成新的外部服务

1. 在 `app/integrations/` 下创建新的模块
2. 实现客户端类
3. 在业务服务中调用

### 运行测试

```bash
# 运行所有测试
pytest

# 运行特定测试
pytest tests/test_chat.py

# 生成覆盖率报告
pytest --cov=app tests/
```

## 部署

### Docker 部署

```bash
# 构建镜像
docker build -t hospital-ai-service:latest .

# 运行容器
docker run -d -p 8000:8000 --env-file .env hospital-ai-service:latest
```

### Docker Compose 部署

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f hospital-ai-service

# 停止服务
docker-compose down
```

## 监控

- **Prometheus**: 收集应用指标
- **Grafana**: 可视化监控面板
- **日志**: 存储在 `logs/` 目录

## 故障排查

### 常见问题

1. **连接 MongoDB 失败**
   - 检查 MongoDB 是否启动
   - 检查 `MONGODB_URI` 配置

2. **调用通义千问失败**
   - 检查 `DASH_SCOPE_API_KEY` 是否正确
   - 检查网络连接

3. **Pinecone 索引不存在**
   - 首次运行会自动创建索引
   - 检查 `PINECONE_API_KEY` 是否正确

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

MIT License