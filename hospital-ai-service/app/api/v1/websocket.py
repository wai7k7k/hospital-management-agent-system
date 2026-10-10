from fastapi import APIRouter, WebSocket, WebSocketDisconnect
from typing import Dict, Optional
from app.utils.logger import log
from datetime import datetime
import json
import asyncio


router = APIRouter(prefix="/v1/ws", tags=["WebSocket"])


class ConnectionManager:
    """WebSocket 连接管理器"""

    def __init__(self):
        """初始化连接管理器"""
        self.active_connections: Dict[str, WebSocket] = {}
        self.heartbeat_interval = 30
        self.owner_loop: Optional[asyncio.AbstractEventLoop] = None

    async def connect(self, websocket: WebSocket, session_id: str):
        """
        建立连接

        Args:
            websocket: WebSocket 连接
            session_id: 会话ID
        """
        await websocket.accept()
        self.owner_loop = asyncio.get_running_loop()
        self.active_connections[session_id] = websocket
        log.info(f"WebSocket 连接建立 - session_id: {session_id}")

        # 启动心跳检测
        asyncio.create_task(self._heartbeat(session_id))

    def disconnect(self, session_id: str):
        """
        断开连接

        Args:
            session_id: 会话ID
        """
        if session_id in self.active_connections:
            del self.active_connections[session_id]
            log.info(f"WebSocket 连接断开 - session_id: {session_id}")

    async def send_message(self, session_id: str, message: dict):
        """
        发送消息

        Args:
            session_id: 会话ID
            message: 消息内容
        """
        if session_id not in self.active_connections:
            log.info(f"WebSocket 患者当前不在线 - session_id: {session_id}")
            return
        try:
            current_loop = asyncio.get_running_loop()
            if self.owner_loop and current_loop is not self.owner_loop:
                # RabbitMQ 消费者运行在工作线程；WebSocket 必须回到 Uvicorn 主事件循环发送。
                future = asyncio.run_coroutine_threadsafe(
                    self._send_json(session_id, message), self.owner_loop
                )
                await asyncio.wrap_future(future)
            else:
                await self._send_json(session_id, message)
        except Exception as e:
            log.error(f"发送消息失败 - session_id: {session_id}: {str(e)}")
            self.disconnect(session_id)

    async def _send_json(self, session_id: str, message: dict):
        connection = self.active_connections.get(session_id)
        if connection:
            await connection.send_json(message)

    async def broadcast(self, message: dict):
        """
        广播消息

        Args:
            message: 消息内容
        """
        for session_id, connection in self.active_connections.items():
            try:
                await connection.send_json(message)
            except Exception as e:
                log.error(f"广播消息失败 - session_id: {session_id}: {str(e)}")
                self.disconnect(session_id)

    async def _heartbeat(self, session_id: str):
        """
        心跳检测

        Args:
            session_id: 会话ID
        """
        while session_id in self.active_connections:
            try:
                await asyncio.sleep(self.heartbeat_interval)

                # 发送心跳包
                await self.send_message(session_id, {
                    "type": "heartbeat",
                    "timestamp": str(datetime.now())
                })

            except Exception as e:
                log.error(f"心跳检测失败 - session_id: {session_id}: {str(e)}")
                self.disconnect(session_id)
                break


# 全局连接管理器
manager = ConnectionManager()


@router.websocket("/chat/{session_id}")
async def websocket_chat(websocket: WebSocket, session_id: str):
    """
    WebSocket 聊天接口

    Args:
        websocket: WebSocket 连接
        session_id: 会话ID
    """
    try:
        # 建立连接
        await manager.connect(websocket, session_id)

        # 发送连接成功消息
        await manager.send_message(session_id, {
            "type": "connected",
            "session_id": session_id,
            "timestamp": str(datetime.now())
        })

        # 接收消息循环
        while True:
            data = await websocket.receive_text()
            message = json.loads(data)

            log.info(f"收到 WebSocket 消息 - session_id: {session_id}, type: {message.get('type')}")

            # 处理不同类型的消息
            if message.get("type") == "chat":
                await _handle_chat_message(session_id, message)
            elif message.get("type") == "ping":
                await manager.send_message(session_id, {"type": "pong"})
            else:
                await manager.send_message(session_id, {
                    "type": "error",
                    "message": "未知消息类型"
                })

    except WebSocketDisconnect:
        manager.disconnect(session_id)
    except Exception as e:
        log.error(f"WebSocket 处理失败: {str(e)}")
        manager.disconnect(session_id)


async def _handle_chat_message(session_id: str, message: dict):
    """
    处理聊天消息

    Args:
        session_id: 会话ID
        message: 消息内容
    """
    try:
        from app.core.graph.workflow import get_workflow
        from app.core.models.request import ChatMessage

        # WebSocket 与 HTTP/SSE 共用同一份 MongoDB 对话历史。
        from app.api.v1.chat import _load_history, _save_message
        content = message.get("content", "")
        history = await _load_history(session_id)

        initial_state = {
            "session_id": session_id,
            "messages": history + [ChatMessage(role="user", content=content)],
            "intent": None,
            "confidence": None,
            "symptoms": None,
            "patient_id": message.get("patient_id"),
            "user_id": message.get("user_id"),
            "triage_result": None,
            "register_result": None,
            "report_result": None,
            "medication_result": None,
            "error": None,
            "response": None,
            "metadata": {}
        }

        # 运行工作流
        workflow = get_workflow()
        final_state = await workflow.run(initial_state)

        await _save_message(session_id, "user", content)
        if final_state.get("response"):
            await _save_message(session_id, "assistant", final_state["response"])

        # 发送响应
        await manager.send_message(session_id, {
            "type": "chat_response",
            "response": final_state.get("response", ""),
            "intent": final_state.get("intent"),
            "confidence": final_state.get("confidence"),
            "timestamp": str(datetime.now())
        })

    except Exception as e:
        log.error(f"处理聊天消息失败: {str(e)}")
        await manager.send_message(session_id, {
            "type": "error",
            "message": "处理失败，请稍后重试"
        })
