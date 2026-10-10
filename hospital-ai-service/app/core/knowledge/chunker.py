"""
文本分块器 — 对文档内容进行切块，支持滑动窗口重叠
"""

import hashlib
from typing import List, Dict, Any


class TextChunker:
    """文本分块：将长文档切分为适合向量检索的短块"""

    def __init__(self):
        self._chunk_size = 500
        self._chunk_overlap = 50

    def update_config(self, chunk_size: int = None, chunk_overlap: int = None):
        if chunk_size is not None:
            self._chunk_size = chunk_size
        if chunk_overlap is not None:
            self._chunk_overlap = chunk_overlap

    async def chunk_text(self, text: str, metadata: Dict[str, Any] = None) -> List[Dict[str, Any]]:
        """对纯文本分块"""
        doc = {"content": text, "metadata": metadata or {}}
        return await self._chunk_one(doc)

    async def chunk_documents(
        self,
        documents: List[Dict[str, Any]],
    ) -> List[Dict[str, Any]]:
        """批量分块"""
        all_chunks = []
        for doc in documents:
            chunks = await self._chunk_one(doc)
            all_chunks.extend(chunks)
        return all_chunks

    async def _chunk_one(self, doc: Dict[str, Any]) -> List[Dict[str, Any]]:
        """对单个文档分块（JSON 数组按条目分，文本按长度切）"""
        content = doc.get("content", "")
        metadata = doc.get("metadata", {})

        # 尝试解析 JSON 数组 — 每个元素独立成一个 chunk
        try:
            import json
            data = json.loads(content)
            if isinstance(data, list):
                chunks = []
                for i, item in enumerate(data):
                    item_text = json.dumps(item, ensure_ascii=False)
                    chunk_id = hashlib.md5(
                        f"{metadata.get('filename', '')}:{i}".encode()
                    ).hexdigest()[:16]
                    # 在 metadata 中保留业务关键字段，搜索时可直接返回
                    chunk_meta = {
                        **metadata,
                        "index": i,
                        "item_id": item.get("id", str(i)) if isinstance(item, dict) else str(i),
                    }
                    if isinstance(item, dict):
                        for k in ("symptoms", "department", "description", "urgency",
                                  "drugA", "drugB", "severity", "category", "suggestion",
                                  "name", "report_type"):
                            if item.get(k):
                                chunk_meta[k] = item[k]
                    chunks.append({
                        "id": chunk_id,
                        "text": item_text,
                        "content": item_text,
                        "metadata": chunk_meta,
                    })
                return chunks
        except (json.JSONDecodeError, ValueError):
            pass

        # 普通文本按长度粗分
        chunk_size = self._chunk_size
        chunks = []
        paragraphs = content.split("\n\n")
        buffer = ""
        for para in paragraphs:
            para = para.strip()
            if not para:
                continue
            if len(buffer) + len(para) < chunk_size:
                buffer += para + "\n"
            else:
                if buffer.strip():
                    chunk_id = hashlib.md5(buffer.encode()).hexdigest()[:16]
                    chunks.append({
                        "id": chunk_id,
                        "text": buffer.strip(),
                        "content": buffer.strip(),
                        "metadata": {**metadata},
                    })
                buffer = para + "\n"
        if buffer.strip():
            chunk_id = hashlib.md5(buffer.encode()).hexdigest()[:16]
            chunks.append({
                "id": chunk_id,
                "text": buffer.strip(),
                "content": buffer.strip(),
                "metadata": {**metadata},
            })

        return chunks
