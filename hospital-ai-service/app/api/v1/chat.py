from fastapi import APIRouter, Depends, HTTPException, Query
from fastapi.responses import StreamingResponse
from app.core.models.request import ChatRequest, ChatResponse
from app.core.models.response import ApiResponse
from app.core.graph.workflow import get_workflow
from app.core.models.request import ChatMessage, IntentType
from app.utils.logger import log
from datetime import datetime
import json
import asyncio

router = APIRouter(prefix="/v1/chat", tags=["聊天"])


# ════════════════════ SSE 流式（必须放在动态路由前）═══════════════════

STATUS_MAP = {
    "intent_recognition": "正在理解您的意图...",
    "triage": "正在进行分诊分析...",
    "register": "正在处理挂号...",
    "report": "正在解读报告...",
    "medication": "正在检查用药方案...",
    "hospital_info": "正在查询医院信息...",
    "examination_event": "正在处理检查事件...",
    "report_event": "正在处理报告事件...",
    "prescription_event": "正在处理处方事件...",
    "response_generation": "正在生成回复...",
}


@router.post("/stream")
async def chat_stream(request: ChatRequest):
    """SSE 流式聊天 —— 实时推送工作流进度 + 逐字输出回复"""
    try:
        workflow = get_workflow()

        history_messages = await _load_history(request.session_id)
        current_message = ChatMessage(role="user", content=request.message)
        all_messages = history_messages + [current_message]

        initial_state = {
            "session_id": request.session_id,
            "messages": all_messages,
            "intent": None, "confidence": None, "symptoms": None,
            "patient_id": request.patient_id, "user_id": request.user_id,
            "triage_result": None, "register_result": None,
            "report_result": None, "medication_result": None,
            "error": None, "response": None, "metadata": {},
            "event_type": None, "event_data": None,
        }

        async def event_generator():
            final_state = initial_state
            response_text = ""

            try:
                async for chunk in workflow.run_stream(initial_state):
                    node_name, node_state = list(chunk.items())[0]
                    if node_name not in STATUS_MAP:
                        continue

                    yield _sse_event("status", {
                        "step": node_name,
                        "message": STATUS_MAP.get(node_name, f"执行中: {node_name}"),
                    })

                    if node_name == "response_generation":
                        final_state = node_state
                        response_text = node_state.get("response", "")

                if response_text:
                    for char in response_text:
                        yield _sse_data({"type": "token", "content": char})
                        if char in "。！？\n":
                            await asyncio.sleep(0.08)
                        else:
                            await asyncio.sleep(0.03)

                intent_val = final_state.get("intent")
                intent_str = intent_val.value if isinstance(intent_val, IntentType) else str(intent_val) if intent_val else None

                yield _sse_event("done", {
                    "session_id": request.session_id,
                    "intent": intent_str,
                    "confidence": final_state.get("confidence"),
                    "response": response_text,
                    "metadata": final_state.get("metadata", {}),
                })

                await _save_message(request.session_id, "user", request.message)
                if response_text:
                    await _save_message(request.session_id, "assistant", response_text)

            except Exception as e:
                log.error(f"SSE 流式失败: {str(e)}")
                yield _sse_event("error", {"message": str(e)})

        return StreamingResponse(
            event_generator(),
            media_type="text/event-stream",
            headers={
                "Cache-Control": "no-cache",
                "Connection": "keep-alive",
                "X-Accel-Buffering": "no",
            },
        )

    except Exception as e:
        log.error(f"SSE 端点失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ════════════════ REST 端点 ════════════════

@router.post("/", response_model=ApiResponse)
async def chat(request: ChatRequest):
    """
    聊天接口 — 带上下文记忆
    """
    try:
        log.info(f"收到聊天请求 - session_id: {request.session_id}")

        history_messages = await _load_history(request.session_id)
        current_message = ChatMessage(role="user", content=request.message)
        all_messages = history_messages + [current_message]

        initial_state = {
            "session_id": request.session_id,
            "messages": all_messages,
            "intent": None, "confidence": None, "symptoms": None,
            "patient_id": request.patient_id, "user_id": request.user_id,
            "triage_result": None, "register_result": None,
            "report_result": None, "medication_result": None,
            "error": None, "response": None, "metadata": {},
            "event_type": None, "event_data": None,
        }

        workflow = get_workflow()
        final_state = await workflow.run(initial_state)

        await _save_message(request.session_id, "user", request.message)
        response_text = final_state.get("response", "")
        if response_text:
            await _save_message(request.session_id, "assistant", response_text)

        response = ChatResponse(
            session_id=request.session_id,
            message=response_text,
            intent=final_state.get("intent"),
            confidence=final_state.get("confidence"),
            metadata=final_state.get("metadata"),
        )

        return ApiResponse(
            code=200, message="success", data=response.dict(),
            timestamp=datetime.now().isoformat(),
        )

    except Exception as e:
        log.error(f"聊天处理失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/history/{session_id}", response_model=ApiResponse)
async def get_chat_history(session_id: str):
    """获取聊天历史"""
    try:
        history = await _load_history(session_id)
        return ApiResponse(
            code=200, message="success",
            data={"history": [{"role": m.role, "content": m.content, "timestamp": m.timestamp.isoformat() if m.timestamp else None} for m in history]},
            timestamp=datetime.now().isoformat(),
        )
    except Exception as e:
        log.error(f"获取聊天历史失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@router.delete("/{session_id}", response_model=ApiResponse)
async def clear_chat_history(session_id: str):
    """清除聊天历史"""
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.clear_session_history(session_id)

        from app.core.services.register_service import RegisterService
        register_service = RegisterService()
        await register_service.clear_state(session_id)

        return ApiResponse(code=200, message="聊天历史已清除", timestamp=datetime.now().isoformat())
    except Exception as e:
        log.error(f"清除聊天历史失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ── 内部辅助函数 ──────────────────────────────────────────────

async def _load_history(session_id: str, limit: int = 20) -> list:
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        raw = await cache.get_session_history(session_id, limit=limit)
        messages = []
        for item in raw:
            messages.append(ChatMessage(
                role=item.get("role", "user"),
                content=item.get("content", ""),
                timestamp=item.get("timestamp"),
            ))
        return messages
    except Exception:
        return []


async def _save_message(session_id: str, role: str, content: str):
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_message(
            session_id=session_id, role=role, content=content,
            metadata={"timestamp": datetime.now().isoformat()},
        )
    except Exception:
        pass


def _sse_event(event: str, data: dict) -> str:
    return f"event: {event}\ndata: {json.dumps(data, ensure_ascii=False)}\n\n"


def _sse_data(data: dict) -> str:
    return f"data: {json.dumps(data, ensure_ascii=False)}\n\n"
