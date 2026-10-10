"""
RabbitMQ 多队列消费者管理器

管理 5 个业务队列的并发消费，将 HIS 事件路由到对应的 handler，
支持幂等性检查（MongoDB eventId 去重）、后台线程运行、优雅关闭。
"""
import json
import threading
import asyncio
from concurrent.futures import ThreadPoolExecutor
from typing import Dict, Any, Callable

from app.integrations.mq.producer import RabbitMQConsumer
from app.utils.config import settings
from app.utils.logger import log


QUEUE_CONFIGS = [
    {"queue_name": "symptom.reported.queue", "routing_key": "patient.symptom.reported"},
    {"queue_name": "register.created.queue", "routing_key": "register.created"},
    {"queue_name": "inspection.ordered.queue", "routing_key": "examination.ordered"},
    {"queue_name": "report.completed.queue", "routing_key": "report.completed"},
    {"queue_name": "prescription.created.queue", "routing_key": "prescription.created"},
]


class ManagedConsumerManager:
    """管理多个 RabbitMQConsumer 的后台运行与路由分发"""

    def __init__(self):
        self._executor = ThreadPoolExecutor(max_workers=len(QUEUE_CONFIGS), thread_name_prefix="rmq-consume")
        self._consumers = []
        self._stop_event = threading.Event()
        self._futures = []
        self._loop = None

    def start_all(self):
        """启动所有队列的消费者"""
        if self._futures:
            log.warning("RabbitMQ 消费者已启动，跳过重复启动")
            return
        self._loop = asyncio.get_running_loop()
        for cfg in QUEUE_CONFIGS:
            future = self._executor.submit(self._run_consumer, cfg)
            self._futures.append(future)
        log.info(f"RabbitMQ 消费者管理器已启动，监听 {len(QUEUE_CONFIGS)} 个队列")

    def _run_consumer(self, cfg: Dict[str, str]):
        """在独立线程中运行单个 consumer"""
        queue_name = cfg["queue_name"]
        routing_key = cfg["routing_key"]

        def on_message(message: Dict[str, Any]):
            return self._dispatch(routing_key, message)

        retry_count = 0
        max_retries = 10

        while not self._stop_event.is_set():
            consumer = None
            try:
                consumer = RabbitMQConsumer(queue_name=queue_name, routing_key=routing_key)
                if self._stop_event.is_set():
                    break
                self._consumers.append(consumer)
                retry_count = 0
                log.info(f"消费者启动成功 - queue: {queue_name}, routing_key: {routing_key}")
                consumer.start_consuming(on_message)
            except Exception as e:
                retry_count += 1
                if retry_count > max_retries:
                    log.error(f"消费者重连次数耗尽 - queue: {queue_name}, 错误: {str(e)}")
                    break
                wait = min(2 ** retry_count, 60)
                log.warning(f"消费者异常，{wait}s 后重连 ({retry_count}/{max_retries}) - queue: {queue_name}: {str(e)}")
                self._stop_event.wait(wait)
            finally:
                if consumer is not None:
                    consumer.close_connection()

    def _dispatch(self, routing_key: str, message: Dict[str, Any]) -> bool:
        """根据 routing_key 分发到对应的 handler"""
        if self._stop_event.is_set():
            return False
        try:
            event_id = message.get("eventId", "unknown")
            event_type = message.get("eventType", "")

            # 患者业务事件没有 patientId 时无法投递通知或创建提醒。
            # 直接确认并丢弃，避免写入 patient: 空会话以及无意义的重复提醒。
            patient_event_keys = {
                "register.created",
                "examination.ordered",
                "report.completed",
                "exam.completed",
                "prescription.created",
                "patient.symptom.reported",
            }
            data = message.get("data", message)
            patient_id = data.get("patientId") if isinstance(data, dict) else None
            if routing_key in patient_event_keys and not str(patient_id or "").strip():
                log.warning(
                    f"跳过缺少 patientId 的无效事件 - routing_key: {routing_key}, "
                    f"event_id: {event_id}"
                )
                return True

            if self._event_exists(event_id):
                log.info(f"重复事件已跳过 - event_id: {event_id}, routing_key: {routing_key}")
                return True

            log.info(f"分发事件 - routing_key: {routing_key}, event_type: {event_type}, event_id: {event_id}")

            if routing_key == "register.created":
                self._run_on_main_loop(self._handle_register_created(message))
            elif routing_key == "examination.ordered":
                self._run_on_main_loop(self._handle_examination_ordered(message))
            elif routing_key in ("report.completed", "exam.completed"):
                self._run_on_main_loop(self._handle_report_completed(message))
            elif routing_key == "prescription.created":
                self._run_on_main_loop(self._handle_prescription_created(message))
            elif routing_key == "patient.symptom.reported":
                self._run_on_main_loop(self._handle_symptom_reported(message))
            else:
                log.warning(f"未知 routing_key: {routing_key}")
                return True

            if not self._mark_processed(event_id):
                log.error(f"事件处理结果无法持久化，将重新入队 - event_id: {event_id}")
                return False
            return True

        except Exception as e:
            log.error(f"事件分发失败 - routing_key: {routing_key}: {str(e)}")
            return False

    def _run_on_main_loop(self, coroutine):
        """所有异步 I/O 都提交到 FastAPI 主循环，避免跨线程复用 Motor 客户端。"""
        if self._loop is None or not self._loop.is_running():
            coroutine.close()
            raise RuntimeError("AI 主事件循环不可用")
        return asyncio.run_coroutine_threadsafe(coroutine, self._loop).result(timeout=180)

    def _event_exists(self, event_id: str) -> bool:
        """处理前只检查幂等记录，不提前标记。"""
        if not event_id or event_id == "unknown":
            return False
        try:
            from app.utils.cache import MongoDBCache
            cache = MongoDBCache()
            return self._run_on_main_loop(cache.event_exists(event_id))
        except Exception:
            return False

    def _mark_processed(self, event_id: str) -> bool:
        if not event_id or event_id == "unknown":
            return True
        try:
            from app.utils.cache import MongoDBCache
            cache = MongoDBCache()
            return self._run_on_main_loop(cache.mark_event_processed(event_id))
        except Exception:
            return False

    # ── 各事件内部处理 ──────────────────────────────────────

    async def _handle_register_created(self, message: Dict[str, Any]):
        from app.core.events.event_handler import handle_register_created
        from app.core.events.event_models import RegisterCreatedEvent

        data = message.get("data", message)
        event = RegisterCreatedEvent(
            register_id=data.get("registerId"),
            patient_id=str(data.get("patientId", "")),
            patient_name=data.get("patientName"),
            dep_id=data.get("depId"),
            dep_name=data.get("depName"),
            user_id=data.get("userId"),
            user_name=data.get("userName"),
            register_date=str(data.get("registerDate", "")) if data.get("registerDate") else None,
            register_price=data.get("registerPrice"),
        )
        await handle_register_created(event)

    async def _handle_examination_ordered(self, message: Dict[str, Any]):
        from app.core.events.event_handler import handle_examination_ordered
        from app.core.events.event_models import ExaminationOrderedEvent

        data = message.get("data", message)
        event = ExaminationOrderedEvent(
            inspect_order_id=data.get("inspectOrderId"),
            patient_id=str(data.get("patientId", "")),
            patient_name=data.get("patientName"),
            exam_id=str(data.get("inspectId", "")) if data.get("inspectId") else None,
            exam_type=data.get("inspectType", data.get("exam_type", "")),
            exam_name=data.get("inspectName", data.get("exam_name", "")),
            doctor_id=str(data.get("userId", "")) if data.get("userId") else None,
            doctor_name=data.get("userName", data.get("doctor_name")),
            department_name=data.get("departmentName", data.get("department_name")),
            order_time=str(data.get("orderDate", "")) if data.get("orderDate") else None,
            register_id=data.get("registerId"),
            instructions=data.get("instructions"),
            estimated_report_time=str(data.get("estimatedReportTime", "")) if data.get("estimatedReportTime") else None,
        )
        await handle_examination_ordered(event)

    async def _handle_report_completed(self, message: Dict[str, Any]):
        from app.core.events.event_handler import handle_report_generated
        from app.core.events.event_models import ReportGeneratedEvent

        data = message.get("data", message)

        report_name = data.get("inspectName", "")
        item_type = data.get("itemType", "")
        indicators = data.get("indicators", [])
        abnormal_indicators = data.get("abnormalIndicators", indicators)

        # 拼接完整报告上下文（包含检查名称、类型、结论、异常指标），
        # 让 LLM 明确知道这是什么检查，不做跨领域猜测
        report_context = {
            "report_name": report_name,
            "item_type": item_type,
            "summary": data.get("summaryConclusion", ""),
            "check_content": data.get("checkContent", ""),
            "check_conclusion": data.get("checkConclusion", ""),
            "indicators": indicators,
            "abnormal_indicators": abnormal_indicators,
        }
        report_data_str = json.dumps(report_context, ensure_ascii=False, default=str)

        event = ReportGeneratedEvent(
            patient_id=str(data.get("patientId", "")),
            patient_name=data.get("patientName"),
            report_id=str(data.get("resultId", "")) if data.get("resultId") else None,
            report_type=item_type,
            report_name=report_name,
            report_data=report_data_str,
            indicators=abnormal_indicators,
            inspect_order_id=data.get("inspectOrderId"),
            register_id=data.get("registerId"),
            generated_time=str(data.get("generatedTime", "")) if data.get("generatedTime") else None,
            is_abnormal=data.get("isAbnormal"),
        )
        await handle_report_generated(event)

    async def _handle_prescription_created(self, message: Dict[str, Any]):
        from app.core.events.event_handler import handle_prescription_created
        from app.core.events.event_models import PrescriptionCreatedEvent, MedicationItem

        data = message.get("data", message)
        meds_raw = data.get("medications", [])
        medications = [MedicationItem(**m) for m in meds_raw] if isinstance(meds_raw, list) else []

        event = PrescriptionCreatedEvent(
            prescription_id=str(data.get("prescriptionId", "")) if data.get("prescriptionId") else None,
            patient_id=str(data.get("patientId", "")),
            patient_name=data.get("patientName"),
            register_id=data.get("registerId"),
            record_id=data.get("recordId"),
            dep_id=data.get("depId"),
            dep_name=data.get("depName"),
            medications=medications,
            doctor_id=str(data.get("userId", "")) if data.get("userId") else None,
            doctor_name=data.get("userName", data.get("doctor_name")),
            diagnosis=data.get("diagnosis"),
            prescription_date=str(data.get("prescriptionDate", "")) if data.get("prescriptionDate") else None,
            notes=data.get("notes"),
        )
        await handle_prescription_created(event)

    async def _handle_symptom_reported(self, message: Dict[str, Any]):
        from app.core.events.event_handler import handle_symptom_reported
        from app.core.events.event_models import PatientSymptomReportedEvent

        data = message.get("data", message)
        event = PatientSymptomReportedEvent(
            patient_id=str(data.get("patientId", "")),
            patient_name=data.get("patientName"),
            symptoms=data.get("symptoms", ""),
            session_id=data.get("sessionId"),
            urgency_self_assessment=data.get("urgencySelfAssessment"),
        )
        await handle_symptom_reported(event)

    def shutdown(self):
        """优雅关闭所有消费者"""
        log.info("关闭 RabbitMQ 消费者管理器...")
        self._stop_event.set()
        for consumer in self._consumers:
            try:
                consumer.close()
            except Exception:
                pass
        self._executor.shutdown(wait=True, cancel_futures=True)
        log.info("RabbitMQ 消费者管理器已关闭")


_consumer_manager: ManagedConsumerManager = None


def get_consumer_manager() -> ManagedConsumerManager:
    global _consumer_manager
    if _consumer_manager is None:
        _consumer_manager = ManagedConsumerManager()
    return _consumer_manager


def shutdown_consumer_manager():
    global _consumer_manager
    if _consumer_manager:
        _consumer_manager.shutdown()
        _consumer_manager = None
