from typing import Optional, List, Dict, Any
from pydantic import BaseModel


class ApiResponse(BaseModel):
    """统一API响应"""
    code: int = 200
    message: str = "success"
    data: Optional[Any] = None
    timestamp: Optional[str] = None


class ErrorResponse(BaseModel):
    """错误响应"""
    code: int
    message: str
    detail: Optional[str] = None
    timestamp: Optional[str] = None


class StreamChunk(BaseModel):
    """流式响应块"""
    chunk: str
    done: bool = False
    metadata: Optional[Dict[str, Any]] = None


class WebSocketMessage(BaseModel):
    """WebSocket 消息"""
    type: str  # message/error/heartbeat/close
    data: Optional[Dict[str, Any]] = None
    timestamp: Optional[str] = None


class SSEEvent(BaseModel):
    """SSE 事件"""
    event: str
    data: str
    id: Optional[str] = None
    retry: Optional[int] = None
