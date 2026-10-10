import asyncio
from typing import Any, Dict, Optional
from datetime import datetime
from app.utils.logger import log


class ReminderService:
    """提醒调度服务 — 基于 asyncio 的轻量级定时器"""

    def __init__(self):
        self._tasks: Dict[str, Any] = {}
        try:
            self._loop = asyncio.get_running_loop()
        except RuntimeError:
            self._loop = None
        self._running = True
        log.info("提醒调度服务初始化完成")

    def schedule_reminder(
        self,
        patient_id: str,
        reminder_type: str,
        message: str,
        delay_seconds: int,
        metadata: Optional[dict] = None,
    ):
        """
        安排一次性提醒

        Args:
            patient_id: 患者ID
            reminder_type: 提醒类型（report_pickup / follow_up / medication）
            message: 提醒消息内容
            delay_seconds: 延迟秒数
            metadata: 额外元数据
        """
        task_key = f"{patient_id}:{reminder_type}:{datetime.now().timestamp()}"

        async def _delayed_reminder():
            await asyncio.sleep(delay_seconds)
            if not self._running:
                return
            await self._fire_reminder(patient_id, reminder_type, message, metadata)
            self._tasks.pop(task_key, None)

        self._tasks[task_key] = self._schedule_task(_delayed_reminder())

        log.info(
            f"提醒已安排 - patient_id: {patient_id}, type: {reminder_type}, "
            f"延迟: {delay_seconds}s ({delay_seconds // 60}min)"
        )

    def schedule_recurring_reminder(
        self,
        patient_id: str,
        reminder_type: str,
        message: str,
        interval_seconds: int,
        times: int = 21,
        metadata: Optional[dict] = None,
    ):
        """
        安排周期性提醒

        Args:
            patient_id: 患者ID
            reminder_type: 提醒类型
            message: 提醒消息内容
            interval_seconds: 间隔秒数
            times: 总提醒次数
            metadata: 额外元数据
        """
        task_key = f"{patient_id}:{reminder_type}:recurring:{datetime.now().timestamp()}"

        async def _recurring_reminder():
            for i in range(times):
                if not self._running:
                    return
                await asyncio.sleep(interval_seconds)
                if not self._running:
                    return
                await self._fire_reminder(
                    patient_id,
                    reminder_type,
                    f"[第{i+1}/{times}次] {message}",
                    metadata,
                )
            self._tasks.pop(task_key, None)
            log.info(f"周期性提醒已完成 - patient_id: {patient_id}, type: {reminder_type}")

        self._tasks[task_key] = self._schedule_task(_recurring_reminder())

        log.info(
            f"周期性提醒已安排 - patient_id: {patient_id}, type: {reminder_type}, "
            f"间隔: {interval_seconds}s, 次数: {times}"
        )

    def cancel_reminders(self, patient_id: str):
        """取消某患者的所有提醒"""
        cancelled = 0
        for key, task in list(self._tasks.items()):
            if key.startswith(patient_id):
                task.cancel()
                self._tasks.pop(key, None)
                cancelled += 1

        if cancelled:
            log.info(f"已取消 {cancelled} 个提醒 - patient_id: {patient_id}")

    def cancel_all(self):
        """取消所有提醒"""
        self._running = False
        for task in self._tasks.values():
            task.cancel()
        self._tasks.clear()
        log.info("所有提醒已取消")

    def _schedule_task(self, coroutine):
        """始终在 FastAPI 主事件循环调度，避免 RabbitMQ 工作线程退出时取消提醒。"""
        try:
            current_loop = asyncio.get_running_loop()
        except RuntimeError:
            current_loop = None

        if self._loop and self._loop.is_running() and current_loop is not self._loop:
            return asyncio.run_coroutine_threadsafe(coroutine, self._loop)
        if current_loop:
            if self._loop is None:
                self._loop = current_loop
            return current_loop.create_task(coroutine)
        raise RuntimeError("提醒调度器没有可用的事件循环")

    async def _fire_reminder(
        self,
        patient_id: str,
        reminder_type: str,
        message: str,
        metadata: Optional[dict] = None,
    ):
        """触发提醒 — 多渠道推送"""
        log.info(f"触发提醒 - patient_id: {patient_id}, type: {reminder_type}")

        # 1. WebSocket 推送
        try:
            from app.api.v1.websocket import manager
            await manager.send_message(f"patient:{patient_id}", {
                "type": "reminder",
                "reminder_type": reminder_type,
                "patient_id": patient_id,
                "message": message,
                "metadata": metadata or {},
                "timestamp": datetime.now().isoformat(),
            })
        except Exception as e:
            log.error(f"WebSocket 提醒推送失败: {str(e)}")

        # 2. RabbitMQ 发布（供短信网关 / App 推送等下游消费）
        try:
            from app.integrations.mq.producer import RabbitMQProducer
            producer = RabbitMQProducer()
            try:
                await producer.publish(
                    exchange="hospital.events",
                    routing_key=f"reminder.{reminder_type}",
                    message={
                        "type": "reminder",
                        "reminder_type": reminder_type,
                        "patient_id": patient_id,
                        "message": message,
                        "metadata": metadata or {},
                        "timestamp": datetime.now().isoformat(),
                    },
                )
            finally:
                producer.close()
        except Exception as e:
            log.error(f"RabbitMQ 提醒发布失败: {str(e)}")

        # 3. MongoDB 持久化
        try:
            from app.utils.cache import MongoDBCache
            cache = MongoDBCache()
            await cache.save_message(
                session_id=f"patient:{patient_id}",
                role="reminder",
                content=message,
                metadata={
                    "type": reminder_type,
                    "delivered": True,
                    "timestamp": datetime.now().isoformat(),
                    **(metadata or {}),
                },
            )
        except Exception as e:
            log.error(f"MongoDB 提醒存储失败: {str(e)}")


# 全局单例
_reminder_service: Optional[ReminderService] = None


def get_reminder_service() -> ReminderService:
    global _reminder_service
    if _reminder_service is None:
        _reminder_service = ReminderService()
    return _reminder_service
