from typing import List, Dict, Any, Optional
from app.utils.config import settings
from app.utils.logger import log


class PineconeClient:
    """Pinecone 向量数据库客户端（兼容 Pinecone SDK v2/v3/v5/v9+）"""

    def __init__(self):
        self.api_key = settings.PINECONE_API_KEY
        self.index_name = settings.PINECONE_INDEX
        self.dimension = settings.PINECONE_DIMENSION
        self.index = None
        self._available = False

        try:
            self.index = self._init_pinecone()
            if self.index is not None:
                self._available = True
                log.info(f"Pinecone 客户端初始化成功 - index: {self.index_name}")
        except Exception as e:
            log.warning(f"Pinecone 客户端初始化失败，将以 stub 模式运行: {str(e)}")
            self._available = False

    def _init_pinecone(self):
        """尝试多种方式初始化 Pinecone，兼容 SDK v2～v9+"""

        # 方式 1: Pinecone v9+ — from pinecone import Pinecone
        try:
            from pinecone import Pinecone
            pc = Pinecone(api_key=self.api_key)
            existing = [idx.name for idx in pc.list_indexes()]
            log.info(f"Pinecone v9+ 检测到索引: {existing}")
            if self.index_name in existing:
                log.info(f"Pinecone v9+ — 索引 '{self.index_name}' 就绪")
                return pc.Index(self.index_name)
            else:
                log.warning(
                    f"Pinecone 索引 '{self.index_name}' 不存在，"
                    f"已有索引: {existing}"
                )
                return None
        except Exception as e:
            log.debug(f"Pinecone v9+ 初始化未命中: {e}")

        # 方式 2: Pinecone v5 — from pinecone.control.pinecone import Pinecone
        try:
            from pinecone.control.pinecone import Pinecone as PC5
            pc = PC5(api_key=self.api_key)
            existing = [idx.name for idx in pc.list_indexes()]
            if self.index_name in existing:
                log.info("Pinecone v5 — 索引就绪")
                return pc.Index(self.index_name)
            else:
                log.warning(f"Pinecone v5 索引 '{self.index_name}' 不在 {existing} 中")
                return None
        except Exception:
            pass

        # 方式 3: Pinecone v2/v3 — pinecone.init() + pinecone.Index()
        try:
            import pinecone as pc2
            pc2.init(api_key=self.api_key, environment=settings.PINECONE_ENVIRONMENT)
            log.info("Pinecone v2/v3 — 初始化成功")
            return pc2.Index(self.index_name)
        except Exception:
            pass

        log.warning("无法初始化任何版本的 Pinecone SDK，将使用 stub 模式")
        return None

    async def search(
        self,
        namespace: str,
        query: str,
        top_k: int = 5,
        filter: Optional[Dict[str, Any]] = None,
    ) -> List[Dict[str, Any]]:
        if not self._available or not self.index:
            return []

        try:
            embedding = await self._generate_embedding(query)
            if not embedding:
                return []

            kwargs = {
                "namespace": namespace,
                "vector": embedding,
                "top_k": top_k,
                "include_metadata": True,
            }
            if filter:
                kwargs["filter"] = filter

            result = self.index.query(**kwargs)

            matches = []
            noise_phrases = (
                "温馨提示", "官方微信公众号", "官方挂号渠道", "谨防诈骗",
                "核酸检测", "拨打120", "很高兴为您服务",
            )
            for match in result.matches:
                item = {"id": match.id, "score": match.score}
                if match.metadata:
                    item.update(match.metadata)
                text = str(item.get("text", item.get("content", "")))
                if namespace == "hospital" and len(text) < 300 and any(phrase in text for phrase in noise_phrases):
                    log.info(f"过滤知识库样板噪声 - id: {match.id}")
                    continue
                matches.append(item)

            log.info(f"Pinecone 搜索完成 - namespace: {namespace}, 结果数: {len(matches)}")
            return matches

        except Exception as e:
            log.error(f"Pinecone 搜索失败: {str(e)}")
            return []

    async def upsert(
        self, namespace: str, vectors: List[Dict[str, Any]]
    ) -> bool:
        if not self._available or not self.index:
            return False

        try:
            records = []
            for v in vectors:
                text = v.get("text", v.get("content", ""))
                embedding = await self._generate_embedding(text)
                if embedding:
                    records.append({
                        "id": v.get("id", ""),
                        "values": embedding,
                        "metadata": v.get("metadata", {}),
                    })

            if records:
                self.index.upsert(vectors=records, namespace=namespace)
                log.info(f"Pinecone 写入完成 - namespace: {namespace}, 数量: {len(records)}")
            return True

        except Exception as e:
            log.error(f"Pinecone 写入失败: {str(e)}")
            return False

    async def delete(
        self,
        namespace: str,
        ids: Optional[List[str]] = None,
        delete_all: bool = False,
    ) -> bool:
        if not self._available or not self.index:
            return False

        try:
            if delete_all:
                self.index.delete(delete_all=True, namespace=namespace)
            elif ids:
                self.index.delete(ids=ids, namespace=namespace)
            log.info(f"Pinecone 删除完成 - namespace: {namespace}")
            return True

        except Exception as e:
            log.error(f"Pinecone 删除失败: {str(e)}")
            return False

    async def _generate_embedding(self, text: str) -> Optional[List[float]]:
        if not text or not text.strip():
            return None

        try:
            from dashscope import TextEmbedding

            resp = TextEmbedding.call(
                model=getattr(settings, 'EMBEDDING_MODEL', 'text-embedding-v3'),
                api_key=settings.DASH_SCOPE_API_KEY,
                input=text,
            )
            if (
                resp.status_code == 200
                and resp.output
                and resp.output.get("embeddings")
            ):
                embedding = resp.output["embeddings"][0].get("embedding", [])
                if embedding:
                    return embedding

            return None

        except Exception as e:
            log.error(f"文本向量生成失败: {str(e)}")
            return None
