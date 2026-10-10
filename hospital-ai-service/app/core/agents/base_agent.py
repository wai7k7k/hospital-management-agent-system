"""
BaseAgent — 所有专业子Agent的抽象基类

提供统一的能力：
  - 记忆管理（MongoDB持久化 + 滑动窗口）
  - LLM调用（通义千问 via dashscope）
  - 向量知识检索（Pinecone）
  - Prompt模板渲染
  - JSON响应解析
  - 记忆回写

每个子类只需定义 AGENT_NAME 和业务方法即可。
"""

import json
from abc import ABC
from typing import Dict, Any, List, Optional

from dashscope import Generation
from app.utils.config import settings
from app.utils.logger import log
from app.core.prompts import render
from app.core.memory import get_memory_provider
from app.integrations.vector.pinecone_client import PineconeClient


class BaseAgent(ABC):
    """所有专业子Agent的基类"""

    AGENT_NAME: str = "base"

    def __init__(self):
        self.api_key = settings.DASH_SCOPE_API_KEY
        self.model = settings.QWEN_MODEL
        self._pinecone: Optional[PineconeClient] = None
        self._memory_provider = get_memory_provider(max_messages=20)

    # ── 记忆 ────────────────────────────────────────────────

    async def _load_memory(self, session_id: str):
        """加载该Agent在指定session下的隔离记忆"""
        return await self._memory_provider.memory_for(self.AGENT_NAME, session_id).load()

    async def remember(self, session_id: str, user_msg: str, assistant_msg: str):
        """保存一轮对话到该Agent的隔离记忆"""
        memory = await self._load_memory(session_id)
        await memory.add("user", user_msg)
        await memory.add("assistant", assistant_msg)

    # ── LLM调用 ─────────────────────────────────────────────

    async def _call_llm(
        self,
        prompt: str,
        max_tokens: int = 800,
        temperature: float = 0.3,
    ):
        """调用大模型"""
        return Generation.call(
            api_key=self.api_key,
            model=self.model,
            prompt=prompt,
            max_tokens=max_tokens,
            temperature=temperature,
        )

    # ── 知识检索 ────────────────────────────────────────────

    @property
    def pinecone(self) -> PineconeClient:
        if self._pinecone is None:
            self._pinecone = PineconeClient()
        return self._pinecone

    async def _retrieve_knowledge(
        self,
        namespace: str,
        query: str,
        top_k: int = 3,
    ) -> List[Dict[str, Any]]:
        """从Pinecone向量库检索参考知识"""
        try:
            return await self.pinecone.search(namespace=namespace, query=query, top_k=top_k)
        except Exception as e:
            log.error(f"[{self.AGENT_NAME}] 知识检索失败: {str(e)}")
            return []

    # ── Prompt构建 ──────────────────────────────────────────

    async def _build_prompt(
        self,
        template_name: str,
        session_id: str,
        **template_vars,
    ) -> str:
        """渲染提示词模板并附加上下文记忆"""
        prompt = render(template_name, **template_vars)
        memory = await self._load_memory(session_id)
        context = memory.to_prompt_context()
        return f"{prompt}\n\n---\n患者历史对话:\n{context}"

    # ── 响应解析 ────────────────────────────────────────────

    @staticmethod
    def _parse_json_response(response, defaults: Dict[str, Any] = None) -> Dict[str, Any]:
        """从LLM响应中提取JSON，失败时返回defaults"""
        defaults = defaults or {}
        try:
            if response.status_code == 200:
                content = response.output.text
                s = content.find("{")
                e = content.rfind("}") + 1
                if s != -1 and e > s:
                    return json.loads(content[s:e])
        except Exception as e:
            log.error(f"解析LLM JSON响应失败: {str(e)}")
        return defaults

    # ── 知识格式化 ──────────────────────────────────────────

    @staticmethod
    def _format_knowledge(knowledge: List[Dict], no_data_msg: str = "（暂无参考知识库数据）") -> str:
        """将知识列表格式化为prompt文本"""
        if not knowledge:
            return no_data_msg
        return "\n".join(
            f"- {k.get('name', k.get('symptoms', ''))}: {k.get('description', '')}"
            for k in knowledge
        )
