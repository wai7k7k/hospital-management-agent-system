"""
向量存储 — 将知识块写入 Pinecone，提供查询和统计服务
"""

from typing import List, Dict, Any, Optional
from app.integrations.vector.pinecone_client import PineconeClient
from app.utils.logger import log


class VectorStore:
    """向量库存储服务"""

    KNOWN_NAMESPACES = ["triage", "medication", "hospital", "report"]

    def __init__(self):
        self._client = PineconeClient()

    async def add_documents(
        self,
        namespace: str,
        chunks: List[Dict[str, Any]],
    ) -> Dict[str, Any]:
        """批量将知识块写入向量库"""
        result = {"success": 0, "failed": 0, "errors": []}

        if not self._client._available:
            return {
                "success": 0,
                "failed": len(chunks),
                "errors": ["Pinecone 不可用"],
            }

        vectors = []
        for chunk in chunks:
            vectors.append({
                "id": chunk.get("id", ""),
                "text": chunk.get("text", chunk.get("content", "")),
                "metadata": chunk.get("metadata", {}),
            })

        batch_size = 100
        for i in range(0, len(vectors), batch_size):
            batch = vectors[i:i + batch_size]
            success = await self._client.upsert(namespace, batch)
            if success:
                result["success"] += len(batch)
                log.info(f"VectorStore 上传进度: {i + len(batch)}/{len(vectors)}")
            else:
                result["failed"] += len(batch)
                result["errors"].append(f"批次 {i // batch_size} 上传失败")

        return result

    async def search(self, namespace: str, query: str, top_k: int = 5) -> List[Dict[str, Any]]:
        """搜索知识库"""
        return await self._client.search(namespace, query, top_k=top_k)

    async def list_namespaces(self) -> List[str]:
        """列出已知的知识库命名空间"""
        return list(self.KNOWN_NAMESPACES)

    async def get_namespace_stats(self, namespace: str) -> Dict[str, Any]:
        """获取命名空间统计信息"""
        return {
            "available": self._client._available,
            "namespace": namespace,
            "document_count": 0,
            "last_updated": None,
        }

    async def delete_documents(
        self,
        namespace: str,
        document_ids: Optional[List[str]] = None,
        delete_all: bool = False,
    ) -> bool:
        """删除知识库中的文档"""
        return await self._client.delete(namespace, ids=document_ids, delete_all=delete_all)
