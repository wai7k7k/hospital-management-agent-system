# 知识库管理系统使用指南

## 概述

医院AI服务知识库管理系统提供完整的知识库管理功能，支持文档上传、智能处理、向量存储和语义检索。

## 系统架构

```
知识库管理系统
├── 文档处理层 (DocumentProcessor)
├── 文本分块层 (TextChunker)
├── 向量存储层 (VectorStore)
├── 知识检索层 (KnowledgeRetriever)
└── API接口层 (knowledge.py)
```

## 功能特性

### 1. 文档处理
- 支持多种格式：.txt, .md, .json, .csv
- 自动文本提取和清理
- 元数据提取和管理
- 批量文档处理

### 2. 智能分块
- 基于段落和语义的智能分块
- 可配置的分块策略
- 重叠窗口处理
- 分块质量优化

### 3. 向量存储
- 基于Pinecone的向量数据库
- 支持多命名空间隔离
- 批量向量操作
- 自动索引维护

### 4. 知识检索
- 语义相似性搜索
- 混合检索（向量+关键词）
- 智能结果重排序
- 上下文感知检索

## API接口

### 1. 上传文档

**接口**: `POST /knowledge/upload`

**参数**:
- `file`: 文档文件（必需）
- `namespace`: 知识库命名空间（必需）
- `chunk_size`: 分块大小（可选，默认500）
- `chunk_overlap`: 分块重叠大小（可选，默认50）

**示例**:
```bash
curl -X POST "http://localhost:8000/knowledge/upload?namespace=triage" \
  -F "file=@medical_knowledge.md"
```

### 2. 上传纯文本

**接口**: `POST /knowledge/upload/text`

**请求体**:
```json
{
  "text": "医学知识内容...",
  "metadata": {
    "source": "manual",
    "category": "internal"
  },
  "chunk_config": {
    "chunk_size": 600,
    "chunk_overlap": 100
  }
}
```

### 3. 查询知识库

**接口**: `POST /knowledge/query`

**请求体**:
```json
{
  "namespace": "triage",
  "query": "胸痛应该挂什么科",
  "top_k": 5,
  "min_score": 0.6
}
```

**响应**:
```json
{
  "query": "胸痛应该挂什么科",
  "namespace": "triage",
  "results": [
    {
      "id": "chunk_1234",
      "score": 0.92,
      "text": "胸痛可能是心脏病...",
      "metadata": {...}
    }
  ],
  "total_results": 3,
  "context": "知识片段 1..."
}
```

### 4. 列出命名空间

**接口**: `GET /knowledge/namespaces`

**响应**:
```json
{
  "namespaces": ["triage", "medication", "hospital", "report", "general"],
  "total": 5
}
```

### 5. 获取统计信息

**接口**: `GET /knowledge/stats/{namespace}`

### 6. 删除文档

**接口**: `DELETE /knowledge/documents/{namespace}`

**参数**:
- `document_ids`: 要删除的文档ID列表（可选）
- `delete_all`: 是否删除所有文档（可选）

### 7. 批量上传

**接口**: `POST /knowledge/batch/upload`

**参数**:
- `files`: 多个文件
- `namespace`: 目标命名空间
- `chunk_size`: 分块大小
- `chunk_overlap`: 分块重叠大小

## 知识库命名空间

系统预定义了以下命名空间：

| 命名空间 | 用途 | 示例内容 |
|---------|------|---------|
| `triage` | 分诊知识 | 症状-科室对应关系 |
| `medication` | 用药知识 | 药物相互作用、禁忌 |
| `hospital` | 医院信息 | 科室介绍、医生排班 |
| `report` | 报告解读 | 检验检查结果解读 |
| `general` | 通用知识 | 医学常识、健康指导 |

## 使用示例

### Python客户端示例

```python
import requests

# 上传文档
with open("medical_knowledge.md", "rb") as f:
    response = requests.post(
        "http://localhost:8000/knowledge/upload?namespace=triage",
        files={"file": f}
    )
    print(response.json())

# 查询知识
query_data = {
    "namespace": "triage",
    "query": "头痛头晕应该挂什么科",
    "top_k": 3
}
response = requests.post(
    "http://localhost:8000/knowledge/query",
    json=query_data
)
print(response.json())
```

### JavaScript客户端示例

```javascript
// 上传文档
const formData = new FormData();
formData.append('file', fileInput.files[0]);

fetch('http://localhost:8000/knowledge/upload?namespace=triage', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));

// 查询知识
fetch('http://localhost:8000/knowledge/query', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    namespace: 'triage',
    query: '胸痛应该挂什么科',
    top_k: 5
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

## 集成到AI Agents

知识库系统已集成到现有的AI Agents中：

```python
from app.core.agents import TriageAgent

agent = TriageAgent()

# Agent会自动使用知识库
result = await agent.analyze(
    symptoms="胸痛、呼吸困难",
    session_id="user_123"
)
```

## 测试

运行知识库测试：

```bash
cd hospital-ai-service
python tests/test_knowledge_base.py
```

## 性能优化建议

1. **分块大小**: 根据文档类型调整，一般500-800字符
2. **批量操作**: 使用批量上传API提高效率
3. **缓存策略**: 启用嵌入向量缓存减少API调用
4. **索引优化**: 定期清理和重建向量索引

## 故障排查

### Pinecone连接失败
- 检查API密钥配置
- 确认网络连接正常
- 验证索引名称正确

### 文档处理失败
- 检查文件格式是否支持
- 确认文件编码为UTF-8
- 验证文件大小不超过限制

### 检索结果不准确
- 调整min_score参数
- 优化分块策略
- 增加训练数据质量

## 扩展开发

### 添加新的文档格式支持

在`DocumentProcessor`中添加新的格式处理器：

```python
async def _extract_pdf_text(self, path: Path) -> str:
    # 实现PDF文本提取
    pass
```

### 自定义分块策略

继承`TextChunker`并实现自定义逻辑：

```python
class CustomChunker(TextChunker):
    async def _split_text(self, text: str) -> List[str]:
        # 自定义分块逻辑
        pass
```

## 维护建议

1. **定期备份**: 重要知识库数据定期备份
2. **质量监控**: 监控检索质量和用户反馈
3. **性能监控**: 使用Prometheus监控系统性能
4. **数据清理**: 定期清理过期或低质量数据

## 技术支持

如有问题，请查看：
- API文档：http://localhost:8000/docs
- 日志文件：logs/目录
- 系统监控：http://localhost:8000/metrics