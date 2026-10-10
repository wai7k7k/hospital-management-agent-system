"""
文档处理器 — 读取 JSON/JSONL/TXT/MD 文件，统一转为内部文档结构
"""

import json
from datetime import datetime
from pathlib import Path
from typing import List, Dict, Any


class DocumentProcessor:
    """文件处理器：读取多种格式并转换为标准文档"""

    def __init__(self):
        self._counter = 0

    async def process_file(self, file_path: str, metadata: Dict[str, Any] = None) -> Dict[str, Any]:
        """处理单个文件，返回标准文档 dict"""
        path = Path(file_path)
        suffix = path.suffix.lower()

        if suffix == ".json":
            doc = await self._process_json(path)
        elif suffix == ".jsonl":
            doc = await self._process_jsonl(path)
        else:
            doc = await self._process_text(path)

        self._counter += 1
        doc["id"] = f"doc_{self._counter}_{datetime.now().timestamp():.0f}"
        doc["text"] = doc.get("text", doc.get("content", ""))
        if metadata:
            doc["metadata"].update(metadata)
        return doc

    async def process_text(self, text: str, metadata: Dict[str, Any] = None) -> Dict[str, Any]:
        """处理纯文本"""
        self._counter += 1
        return {
            "id": f"doc_{self._counter}_{datetime.now().timestamp():.0f}",
            "text": text,
            "content": text,
            "metadata": metadata or {},
        }

    async def process_directory(self, dir_path: str) -> List[Dict[str, Any]]:
        """递归扫描目录，处理所有支持的文件"""
        documents = []
        path = Path(dir_path)

        for file_path in path.rglob("*"):
            if file_path.is_file():
                suffix = file_path.suffix.lower()
                if suffix in (".json", ".jsonl", ".txt", ".md"):
                    try:
                        doc = await self.process_file(str(file_path))
                        documents.append(doc)
                    except Exception as e:
                        print(f"  ! 跳过文件: {file_path.name} — {e}")

        return documents

    async def _process_json(self, path: Path) -> Dict[str, Any]:
        with open(path, "r", encoding="utf-8") as f:
            raw = f.read()

        try:
            data = json.loads(raw)
        except json.JSONDecodeError:
            # 文件后缀是 .json 但内容不是合法 JSON（如 LLM 提取的纯文本），回退为文本
            return {
                "content": raw,
                "metadata": {
                    "filename": path.name,
                    "file_type": "text",
                    "file_size": path.stat().st_size,
                },
            }

        return {
            "content": json.dumps(data, ensure_ascii=False),
            "metadata": {
                "filename": path.name,
                "file_type": "json",
                "file_size": path.stat().st_size,
                "item_count": len(data) if isinstance(data, list) else 1,
            },
        }

    async def _process_jsonl(self, path: Path) -> Dict[str, Any]:
        lines = []
        with open(path, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if line:
                    lines.append(json.loads(line))

        return {
            "content": json.dumps(lines, ensure_ascii=False),
            "metadata": {
                "filename": path.name,
                "file_type": "jsonl",
                "file_size": path.stat().st_size,
                "line_count": len(lines),
            },
        }

    async def _process_text(self, path: Path) -> Dict[str, Any]:
        with open(path, "r", encoding="utf-8") as f:
            content = f.read()

        return {
            "content": content,
            "metadata": {
                "filename": path.name,
                "file_type": path.suffix.lstrip("."),
                "file_size": path.stat().st_size,
            },
        }
