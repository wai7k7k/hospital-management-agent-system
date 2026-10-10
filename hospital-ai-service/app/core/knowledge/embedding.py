"""
文本嵌入服务 - 基于通义千问的文本向量化

提供文本嵌入功能：
  - 单文本嵌入生成
  - 批量文本嵌入
  - 嵌入缓存管理
  - 嵌入质量监控
"""

from typing import List, Optional
import asyncio
from functools import lru_cache
from app.utils.config import settings
from app.utils.logger import log


class EmbeddingService:
    """文本嵌入服务"""

    def __init__(self):
        self.api_key = settings.DASH_SCOPE_API_KEY
        self.model = "text-embedding-v2"
        self.dimension = settings.PINECONE_DIMENSION
        self._cache_enabled = True

    async def generate_embedding(self, text: str) -> Optional[List[float]]:
        """
        生成文本嵌入向量

        Args:
            text: 输入文本

        Returns:
            嵌入向量，失败返回None
        """
        if not text or not text.strip():
            log.warning("输入文本为空，无法生成嵌入")
            return None

        cache_key = f"emb:{hash(text)}"
        if self._cache_enabled:
            cached = self._get_cached_embedding(cache_key)
            if cached:
                return cached

        try:
            from dashscope import TextEmbedding

            resp = TextEmbedding.call(
                model=self.model,
                api_key=self.api_key,
                input=text,
            )

            if resp.status_code == 200 and resp.output:
                embeddings = resp.output.get("embeddings", [])
                if embeddings and len(embeddings) > 0:
                    embedding = embeddings[0].get("embedding", [])
                    if embedding and len(embedding) == self.dimension:
                        if self._cache_enabled:
                            self._cache_embedding(cache_key, embedding)
                        return embedding

            log.error(f"嵌入生成失败 - status: {resp.status_code}, message: {resp.message}")
            return None

        except Exception as e:
            log.error(f"嵌入生成异常: {str(e)}")
            return None

    async def generate_batch_embeddings(
        self,
        texts: List[str],
        batch_size: int = 10,
        max_concurrent: int = 5
    ) -> List[Optional[List[float]]]:
        """
        批量生成文本嵌入

        Args:
            texts: 文本列表
            batch_size: 每批处理数量
            max_concurrent: 最大并发数

        Returns:
            嵌入向量列表
        """
        if not texts:
            return []

        results = [None] * len(texts)
        semaphore = asyncio.Semaphore(max_concurrent)

        async def process_batch(start_idx: int):
            async with semaphore:
                batch = texts[start_idx:start_idx + batch_size]
                batch_results = []

                for i, text in enumerate(batch):
                    embedding = await self.generate_embedding(text)
                    batch_results.append(embedding)

                for i, result in enumerate(batch_results):
                    results[start_idx + i] = result

        tasks = []
        for i in range(0, len(texts), batch_size):
            tasks.append(process_batch(i))

        await asyncio.gather(*tasks, return_exceptions=True)

        success_count = sum(1 for r in results if r is not None)
        log.info(f"批量嵌入生成完成 - 总数: {len(texts)}, 成功: {success_count}")

        return results

    async def compute_similarity(
        self,
        embedding1: List[float],
        embedding2: List[float]
    ) -> float:
        """
        计算两个嵌入向量的余弦相似度

        Args:
            embedding1: 第一个嵌入向量
            embedding2: 第二个嵌入向量

        Returns:
            相似度分数 (0-1)
        """
        if not embedding1 or not embedding2:
            return 0.0

        if len(embedding1) != len(embedding2):
            log.warning(f"嵌入维度不匹配: {len(embedding1)} vs {len(embedding2)}")
            return 0.0

        try:
            import numpy as np

            vec1 = np.array(embedding1)
            vec2 = np.array(embedding2)

            dot_product = np.dot(vec1, vec2)
            norm1 = np.linalg.norm(vec1)
            norm2 = np.linalg.norm(vec2)

            if norm1 == 0 or norm2 == 0:
                return 0.0

            similarity = dot_product / (norm1 * norm2)
            return float(similarity)

        except Exception as e:
            log.error(f"相似度计算失败: {str(e)}")
            return 0.0

    def _get_cached_embedding(self, cache_key: str) -> Optional[List[float]]:
        """从缓存获取嵌入（简化版，实际可使用Redis）"""
        return None

    def _cache_embedding(self, cache_key: str, embedding: List[float]):
        """缓存嵌入向量（简化版，实际可使用Redis）"""
        pass

    async def validate_embedding(self, embedding: List[float]) -> bool:
        """
        验证嵌入向量的有效性

        Args:
            embedding: 嵌入向量

        Returns:
            是否有效
        """
        if not embedding:
            return False

        if len(embedding) != self.dimension:
            log.warning(f"嵌入维度不正确: 期望 {self.dimension}, 实际 {len(embedding)}")
            return False

        try:
            import numpy as np
            vec = np.array(embedding)

            if np.any(np.isnan(vec)):
                log.warning("嵌入向量包含NaN值")
                return False

            if np.any(np.isinf(vec)):
                log.warning("嵌入向量包含无穷值")
                return False

            norm = np.linalg.norm(vec)
            if norm == 0:
                log.warning("嵌入向量范数为0")
                return False

            return True

        except Exception as e:
            log.error(f"嵌入验证失败: {str(e)}")
            return False
