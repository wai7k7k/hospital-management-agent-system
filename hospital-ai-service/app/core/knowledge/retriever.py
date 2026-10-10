"""
知识检索引擎 — 封装 Pinecone 搜索 + LLM 上下文生成
"""

from typing import Dict, Any, List
from app.core.knowledge.vector_store import VectorStore
from app.utils.logger import log


class KnowledgeRetriever:
    """知识检索引擎"""

    def __init__(self):
        self._vs = VectorStore()

    async def retrieve_with_context(
        self,
        namespace: str,
        query: str,
        top_k: int = 5,
        min_score: float = 0.6,
    ) -> Dict[str, Any]:
        """
        检索知识并生成 LLM 上下文

        Returns:
            {"results": [...], "result_count": N, "context": "..."}
        """
        try:
            results = await self._vs.search(namespace, query, top_k=top_k)
            filtered = [r for r in results if r.get("score", 0) >= min_score]

            context_parts = []
            for r in filtered:
                parts = []
                for k in ("symptoms", "department", "description", "urgency",
                          "drugA", "drugB", "severity", "suggestion", "name"):
                    if r.get(k):
                        parts.append(f"{k}: {r[k]}")
                if not parts:
                    parts.append(r.get("text", r.get("content", ""))[:200])
                context_parts.append("\n".join(parts))

            context = "\n\n".join(context_parts) if context_parts else None

            log.info(f"KnowledgeRetriever - namespace: {namespace}, found: {len(filtered)}")
            return {
                "results": filtered,
                "result_count": len(filtered),
                "context": context,
            }

        except Exception as e:
            log.error(f"KnowledgeRetriever 检索失败: {e}")
            return {"results": [], "result_count": 0, "context": None}
