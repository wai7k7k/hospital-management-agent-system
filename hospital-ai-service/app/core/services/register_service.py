"""
挂号服务 — 编排层

委托给 RegisterAgent 执行多轮挂号对话，本层负责：
  - 从 MongoDB 加载/保存挂号状态
  - 调用 RegisterAgent.process() 推进对话
  - 返回 RegisterResponse
"""

from typing import Dict, Any, Optional

from app.utils.logger import log
from app.core.models.request import RegisterResponse
from app.core.agents import RegisterAgent


STATE_KEY_PREFIX = "register_state"


class RegisterService:
    """挂号编排服务"""

    def __init__(self):
        self._agent: RegisterAgent = None

    @property
    def agent(self) -> RegisterAgent:
        if self._agent is None:
            self._agent = RegisterAgent()
        return self._agent

    async def process_register_request(
        self,
        session_id: str = "default",
        message: str = "",
        patient_id: Optional[str] = None,
        pre_filled: Optional[Dict[str, str]] = None,
    ) -> RegisterResponse:
        """
        处理一轮挂号对话（多轮状态机）

        1. 从 MongoDB 加载当前挂号状态 (step, collected_info)
        2. 如果首次进入且有 pre_filled 信息，预填到 collected_info
        3. 调用 RegisterAgent 推进状态
        4. 保存新状态回 MongoDB
        5. 返回响应
        """
        log.info(f"[RegisterService] 处理挂号请求 - session: {session_id}")

        # 1. 加载状态
        state = await self._load_state(session_id)
        step = state.get("step", "collect_info")
        collected_info = state.get("collected_info", {})

        # 语义检测：是否触发了"挂号"或"预约"意图的关键词
        _has_register_intent = (
            "挂号" in message or "预约" in message or
            "帮我挂" in message or "我想挂" in message or "我要挂" in message
        )
        if _has_register_intent and step == "done" and "成功" not in message:
            step = "collect_info"
            collected_info = dict(pre_filled) if pre_filled else {}
            log.info(f"[RegisterService] 检测到挂号意图，重置状态")

        # 首次进入挂号流程时，自动预填分诊结果中的科室
        if step == "collect_info" and not any(collected_info.values()) and pre_filled:
            for field in ("patient_name", "card_id", "department", "date", "time_slot"):
                if pre_filled.get(field):
                    collected_info[field] = pre_filled[field]
            log.info(f"[RegisterService] 预填信息: {list(collected_info.keys())}")

        # 2. 推进状态机
        result = await self.agent.process(
            step=step,
            collected_info=collected_info,
            user_message=message,
            patient_id=patient_id,
        )

        # 3. 保存状态
        await self._save_state(session_id, {
            "step": result["step"],
            "collected_info": result["collected_info"],
        })

        # 4. 返回响应
        response_text = result.get("response")
        if response_text is None:
            response_text = ""  # step="done" 时交还上游，不填回复
        return RegisterResponse(
            success=result.get("register_result", {}).get("success", False) if result.get("register_result") else
                     (result["step"] == "done" and collected_info != result.get("collected_info", {})),
            appointment_id=result.get("register_result", {}).get("appointment_id") if result.get("register_result") else None,
            message=response_text,
        )

    # ── 状态持久化 ──────────────────────────────────────────

    async def _load_state(self, session_id: str) -> Dict[str, Any]:
        try:
            from app.utils.cache import MongoDBCache
            cache = MongoDBCache()
            state = await cache.get_session_state(f"{STATE_KEY_PREFIX}:{session_id}")
            return state or {"step": "collect_info", "collected_info": {}}
        except Exception as e:
            log.error(f"[RegisterService] 加载挂号状态失败: {e}")
            return {"step": "collect_info", "collected_info": {}}

    async def _save_state(self, session_id: str, state: Dict[str, Any]):
        try:
            from app.utils.cache import MongoDBCache
            cache = MongoDBCache()
            saved = await cache.save_session_state(f"{STATE_KEY_PREFIX}:{session_id}", state)
            if not saved:
                raise RuntimeError("挂号状态保存失败，请稍后重试")
        except Exception as e:
            log.error(f"[RegisterService] 保存挂号状态失败: {e}")
            raise

    async def clear_state(self, session_id: str):
        """清除挂号状态"""
        await self._save_state(session_id, {"step": "collect_info", "collected_info": {}})
