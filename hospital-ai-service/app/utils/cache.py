from motor.motor_asyncio import AsyncIOMotorClient
from typing import Optional, List, Dict, Any
from app.utils.config import settings
from app.utils.logger import log
from datetime import datetime


class MongoDBCache:
    """MongoDB 缓存客户端"""

    def __init__(self):
        """初始化 MongoDB 客户端"""
        try:
            self.client = AsyncIOMotorClient(settings.MONGODB_URI)
            self.db = self.client[settings.MONGODB_DATABASE]

            log.info(f"MongoDB 客户端初始化成功 - database: {settings.MONGODB_DATABASE}")

        except Exception as e:
            log.error(f"MongoDB 客户端初始化失败: {str(e)}")
            raise

    async def save_message(
        self,
        session_id: str,
        role: str,
        content: str,
        metadata: Optional[Dict[str, Any]] = None
    ) -> bool:
        """
        保存消息

        Args:
            session_id: 会话ID
            role: 角色 (user/assistant/system)
            content: 消息内容
            metadata: 元数据

        Returns:
            是否成功
        """
        try:
            message = {
                "session_id": session_id,
                "role": role,
                "content": content,
                "metadata": metadata or {},
                "timestamp": datetime.now()
            }

            await self.db.messages.insert_one(message)

            log.info(f"消息保存成功 - session_id: {session_id}")
            return True

        except Exception as e:
            log.error(f"保存消息失败: {str(e)}")
            return False

    async def get_session_history(self, session_id: str, limit: int = 50) -> List[Dict[str, Any]]:
        """
        获取会话历史

        Args:
            session_id: 会话ID
            limit: 消息数量限制

        Returns:
            消息列表
        """
        try:
            cursor = self.db.messages.find(
                {"session_id": session_id}
            ).sort("timestamp", -1).limit(limit)

            messages = []
            async for message in cursor:
                messages.append({
                    "role": message["role"],
                    "content": message["content"],
                    "timestamp": message["timestamp"].isoformat()
                })

            log.info(f"获取会话历史成功 - session_id: {session_id}, count: {len(messages)}")

            return list(reversed(messages))

        except Exception as e:
            log.error(f"获取会话历史失败: {str(e)}")
            return []

    async def clear_session_history(self, session_id: str) -> bool:
        """
        清除会话历史

        Args:
            session_id: 会话ID

        Returns:
            是否成功
        """
        try:
            result = await self.db.messages.delete_many({"session_id": session_id})

            log.info(f"清除会话历史成功 - session_id: {session_id}, deleted: {result.deleted_count}")

            return True

        except Exception as e:
            log.error(f"清除会话历史失败: {str(e)}")
            return False

    async def save_session_state(
        self,
        session_id: str,
        state: Dict[str, Any]
    ) -> bool:
        """
        保存会话状态

        Args:
            session_id: 会话ID
            state: 状态数据

        Returns:
            是否成功
        """
        try:
            await self.db.session_states.update_one(
                {"session_id": session_id},
                {
                    "$set": {
                        "state": state,
                        "updated_at": datetime.now()
                    }
                },
                upsert=True
            )

            log.info(f"会话状态保存成功 - session_id: {session_id}")
            return True

        except Exception as e:
            log.error(f"保存会话状态失败: {str(e)}")
            return False

    async def get_session_state(self, session_id: str) -> Optional[Dict[str, Any]]:
        """
        获取会话状态

        Args:
            session_id: 会话ID

        Returns:
            状态数据
        """
        try:
            doc = await self.db.session_states.find_one({"session_id": session_id})

            if doc:
                return doc.get("state")

            return None

        except Exception as e:
            log.error(f"获取会话状态失败: {str(e)}")
            return None

    async def close(self):
        """关闭客户端"""
        self.client.close()
        log.info("MongoDB 客户端已关闭")

    async def event_exists(self, event_id: str) -> bool:
        """检查事件是否已处理（幂等性）"""
        try:
            doc = await self.db.processed_events.find_one({"event_id": event_id})
            return doc is not None
        except Exception as e:
            log.error(f"检查事件存在性失败: {str(e)}")
            return False

    async def mark_event_processed(self, event_id: str) -> bool:
        """标记事件已处理"""
        try:
            await self.db.processed_events.update_one(
                {"event_id": event_id},
                {"$setOnInsert": {
                    "event_id": event_id,
                    "processed_at": datetime.now(),
                }},
                upsert=True,
            )
            return True
        except Exception as e:
            log.error(f"标记事件已处理失败: {str(e)}")
            return False
