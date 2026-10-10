"""
聊天记忆系统 — 模仿 Java LangChain4j 的 ChatMemoryProvider / MessageWindowChatMemory 模式

架构:
  MongoChatMemoryStore  →  MongoDB 持久化存储（单例）
  ChatMemoryProvider    →  记忆工厂（类比 Spring @Bean）
  AgentMemory           →  滑动窗口记忆（每个 Agent 独立窗口，max 20 条）
  AgentMemoryFactory    →  按 Agent 名称创建隔离记忆
"""

from typing import List, Dict, Optional, Any
from datetime import datetime
from motor.motor_asyncio import AsyncIOMotorClient
from app.utils.config import settings
from app.utils.logger import log


# ═══════════════════════════════════════════════════════════════
# MongoChatMemoryStore — 类比 Java 的 MongoChatMemoryStore
# ═══════════════════════════════════════════════════════════════

class MongoChatMemoryStore:
    """MongoDB 持久化记忆存储"""

    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._initialized = False
        return cls._instance

    def __init__(self):
        if self._initialized:
            return
        try:
            self.client = AsyncIOMotorClient(settings.MONGODB_URI)
            self.db = self.client[settings.MONGODB_DATABASE]
            self.collection = self.db["agent_memories"]
            self._initialized = True
            log.info("MongoChatMemoryStore 初始化成功")
        except Exception as e:
            log.error(f"MongoChatMemoryStore 初始化失败: {str(e)}")
            raise

    async def get_messages(self, memory_id: str) -> List[Dict[str, Any]]:
        """从 MongoDB 加载记忆"""
        try:
            cursor = self.collection.find({"memory_id": memory_id}).sort("timestamp", -1)
            messages = []
            async for doc in cursor:
                messages.append({"role": doc["role"], "content": doc["content"]})
            messages.reverse()
            return messages
        except Exception as e:
            log.error(f"加载记忆失败 - memory_id: {memory_id}: {str(e)}")
            return []

    async def add_message(self, memory_id: str, role: str, content: str) -> None:
        """持久化一条消息"""
        try:
            await self.collection.insert_one({
                "memory_id": memory_id,
                "role": role,
                "content": content,
                "timestamp": datetime.now(),
            })
        except Exception as e:
            log.error(f"持久化消息失败 - memory_id: {memory_id}: {str(e)}")

    async def clear(self, memory_id: str) -> None:
        """清除指定 memory_id 的所有消息"""
        try:
            result = await self.collection.delete_many({"memory_id": memory_id})
            log.info(f"清除记忆 - memory_id: {memory_id}, deleted: {result.deleted_count}")
        except Exception as e:
            log.error(f"清除记忆失败 - memory_id: {memory_id}: {str(e)}")

    async def close(self):
        if hasattr(self, 'client'):
            self.client.close()


# ═══════════════════════════════════════════════════════════════
# AgentMemory — 类比 Java 的 MessageWindowChatMemory
# ═══════════════════════════════════════════════════════════════

class AgentMemory:
    """
    单个 Agent 的滑动窗口记忆

    - 从 MongoDB 加载历史
    - 内存中维护最近 max_messages 条
    - 每次 add 同时持久化到 MongoDB
    """

    def __init__(
        self,
        memory_id: str,
        store: MongoChatMemoryStore,
        max_messages: int = 20,
    ):
        self.memory_id = memory_id
        self._store = store
        self._max_messages = max_messages
        self._buffer: List[Dict[str, str]] = []

    async def load(self):
        """从 MongoDB 加载历史消息到内存窗口"""
        all_msgs = await self._store.get_messages(self.memory_id)
        self._buffer = all_msgs[-self._max_messages:]
        return self

    async def add(self, role: str, content: str) -> None:
        """添加消息（内存 + MongoDB 双写）"""
        entry = {"role": role, "content": content}
        self._buffer.append(entry)

        if len(self._buffer) > self._max_messages:
            self._buffer = self._buffer[-self._max_messages:]

        await self._store.add_message(self.memory_id, role, content)

    def messages(self) -> List[Dict[str, str]]:
        """返回当前窗口内的消息列表"""
        return list(self._buffer)

    def to_prompt_context(self) -> str:
        """将记忆格式化为 LLM prompt 上下文"""
        if not self._buffer:
            return "（暂无对话历史）"
        lines = []
        for m in self._buffer:
            role_label = "患者" if m["role"] == "user" else "助手"
            lines.append(f"{role_label}: {m['content']}")
        return "\n".join(lines)

    async def clear(self) -> None:
        """清除记忆"""
        self._buffer.clear()
        await self._store.clear(self.memory_id)


# ═══════════════════════════════════════════════════════════════
# ChatMemoryProvider — 类比 Java 的 @Bean ChatMemoryProvider
# ═══════════════════════════════════════════════════════════════

class ChatMemoryProvider:
    """
    记忆提供者工厂

    用法（类比 Java Spring Bean 注入）:
        store = MongoChatMemoryStore()
        provider = ChatMemoryProvider(store, max_messages=20)
        memory = await provider.memory_for("triage", "session_123").load()
    """

    def __init__(self, store: MongoChatMemoryStore = None, max_messages: int = 20):
        self._store = store or MongoChatMemoryStore()
        self._max_messages = max_messages

    def memory_for(self, agent_name: str, session_id: str) -> AgentMemory:
        """为指定 Agent + Session 创建隔离记忆"""
        memory_id = f"{agent_name}:{session_id}"
        return AgentMemory(memory_id, self._store, self._max_messages)


# ═══════════════════════════════════════════════════════════════
# 全局单例
# ═══════════════════════════════════════════════════════════════

_store: Optional[MongoChatMemoryStore] = None
_provider: Optional[ChatMemoryProvider] = None


def get_memory_store() -> MongoChatMemoryStore:
    global _store
    if _store is None:
        _store = MongoChatMemoryStore()
    return _store


def get_memory_provider(max_messages: int = 20) -> ChatMemoryProvider:
    global _provider
    if _provider is None:
        _provider = ChatMemoryProvider(get_memory_store(), max_messages=max_messages)
    return _provider
