"""
知识检索服务 - 智能知识检索和搜索

提供高级检索功能：
  - 混合检索（向量+关键词）
  - 语义搜索
  - 知识重排序
  - 检索结果优化
"""

from typing import List, Dict, Any, Optional
from datetime import datetime
from app.core.knowledge.vector_store import VectorStore
from app.core.knowledge.embedding import EmbeddingService
from app.utils.logger import log


class KnowledgeRetriever:
    """知识检索服务"""

    def __init__(
        self,
        vector_store: Optional[VectorStore] = None,
        embedding_service: Optional[EmbeddingService] = None
    ):
        self.vector_store = vector_store or VectorStore()
        self.embedding_service = embedding_service or EmbeddingService()

    async def retrieve(
        self,
        namespace: str,
        query: str,
        top_k: int = 5,
        min_score: float = 0.6,
        filters: Optional[Dict[str, Any]] = None,
        rerank: bool = True
    ) -> List[Dict[str, Any]]:
        """
        检索相关知识

        Args:
            namespace: 知识库命名空间
            query: 查询文本
            top_k: 返回结果数量
            min_score: 最小相似度分数
            filters: 过滤条件
            rerank: 是否重排序

        Returns:
            检索结果列表
        """
        try:
            results = await self.vector_store.search(
                namespace=namespace,
                query=query,
                top_k=top_k * 2,  # 获取更多结果用于重排序
                filter=filters,
                min_score=min_score
            )

            if not results:
                log.info(f"未找到相关知识 - namespace: {namespace}, query: {query[:30]}...")
                return []

            if rerank and len(results) > 1:
                results = await self._rerank_results(query, results)

            final_results = results[:top_k]

            log.info(f"知识检索完成 - namespace: {namespace}, 找到: {len(final_results)} 条结果")
            return final_results

        except Exception as e:
            log.error(f"知识检索失败: {str(e)}")
            return []

    async def retrieve_with_context(
        self,
        namespace: str,
        query: str,
        context_window: int = 3,
        **kwargs
    ) -> Dict[str, Any]:
        """
        带上下文的知识检索

        Args:
            namespace: 命名空间
            query: 查询文本
            context_window: 上下文窗口大小
            **kwargs: 其他检索参数

        Returns:
            包含检索结果和上下文的字典
        """
        try:
            results = await self.retrieve(namespace, query, **kwargs)

            if not results:
                return {
                    "query": query,
                    "results": [],
                    "context": "",
                    "has_context": False
                }

            context_parts = []
            for i, result in enumerate(results[:context_window]):
                score = result.get("score", 0)
                content = result.get("text", result.get("content", ""))
                metadata = result.get("metadata", {})

                context_parts.append(
                    f"[知识片段 {i+1}] (相似度: {score:.2f})\n"
                    f"{content}\n"
                    f"来源: {metadata.get('source', '未知')}"
                )

            context = "\n\n".join(context_parts)

            return {
                "query": query,
                "results": results,
                "context": context,
                "has_context": True,
                "result_count": len(results)
            }

        except Exception as e:
            log.error(f"带上下文检索失败: {str(e)}")
            return {
                "query": query,
                "results": [],
                "context": "",
                "has_context": False,
                "error": str(e)
            }

    async def multi_namespace_retrieve(
        self,
        namespaces: List[str],
        query: str,
        top_k_per_namespace: int = 3,
        **kwargs
    ) -> Dict[str, List[Dict[str, Any]]]:
        """
        多命名空间检索

        Args:
            namespaces: 命名空间列表
            query: 查询文本
            top_k_per_namespace: 每个命名空间返回结果数
            **kwargs: 其他检索参数

        Returns:
            按命名空间分组的检索结果
        """
        results = {}

        for namespace in namespaces:
            try:
                namespace_results = await self.retrieve(
                    namespace=namespace,
                    query=query,
                    top_k=top_k_per_namespace,
                    **kwargs
                )
                results[namespace] = namespace_results

            except Exception as e:
                log.error(f"命名空间 {namespace} 检索失败: {str(e)}")
                results[namespace] = []

        total_results = sum(len(r) for r in results.values())
        log.info(f"多命名空间检索完成 - 查询: {query[:30]}..., 总结果: {total_results}")

        return results

    async def _rerank_results(
        self,
        query: str,
        results: List[Dict[str, Any]]
    ) -> List[Dict[str, Any]]:
        """
        重排序检索结果

        Args:
            query: 原始查询
            results: 检索结果

        Returns:
            重排序后的结果
        """
        try:
            query_embedding = await self.embedding_service.generate_embedding(query)
            if not query_embedding:
                return results

            reranked_results = []
            for result in results:
                original_score = result.get("score", 0)
                text = result.get("text", result.get("content", ""))

                text_embedding = await self.embedding_service.generate_embedding(text)
                if text_embedding:
                    semantic_score = await self.embedding_service.compute_similarity(
                        query_embedding,
                        text_embedding
                    )

                    combined_score = 0.7 * original_score + 0.3 * semantic_score
                    result["rerank_score"] = combined_score
                else:
                    result["rerank_score"] = original_score

                reranked_results.append(result)

            reranked_results.sort(key=lambda x: x.get("rerank_score", 0), reverse=True)
            return reranked_results

        except Exception as e:
            log.error(f"重排序失败: {str(e)}")
            return results

    async def get_similar_documents(
        self,
        namespace: str,
        document_id: str,
        top_k: int = 5
    ) -> List[Dict[str, Any]]:
        """
        获取相似文档

        Args:
            namespace: 命名空间
            document_id: 文档ID
            top_k: 返回结果数量

        Returns:
            相似文档列表
        """
        try:
            results = await self.vector_store.search(
                namespace=namespace,
                query=document_id,  # 使用文档ID作为查询
                top_k=top_k + 1,  # +1 因为会包含自己
                min_score=0.0
            )

            similar_docs = [
                r for r in results
                if r.get("id") != document_id
            ][:top_k]

            log.info(f"找到 {len(similar_docs)} 个相似文档 - document_id: {document_id}")
            return similar_docs

        except Exception as e:
            log.error(f"获取相似文档失败: {str(e)}")
            return []
