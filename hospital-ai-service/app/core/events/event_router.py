from fastapi import APIRouter, BackgroundTasks
from app.core.events.event_models import (
    EventEnvelope,
    EventType,
    ExaminationOrderedEvent,
    ReportGeneratedEvent,
    PrescriptionCreatedEvent,
    RegisterCreatedEvent,
    PatientSymptomReportedEvent,
    ExamCompletedEvent,
)
from app.core.models.response import ApiResponse
from app.utils.logger import log
from datetime import datetime

router = APIRouter(prefix="/v1/events", tags=["HIS事件"])


@router.post("/symptom-reported", response_model=ApiResponse)
async def on_symptom_reported(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    患者主诉症状 → AI 分诊分析并推荐就诊科室

    HIS 系统（或患者端）提交症状后调用此接口。
    AI 将分析症状并返回推荐科室、紧急程度、分诊依据。
    """
    try:
        event = PatientSymptomReportedEvent(**envelope.data)
        log.info(f"收到患者症状上报事件 - patient_id: {event.patient_id}")
        bg.add_task(_dispatch_symptom_reported, event)
        return ApiResponse(
            code=200,
            message="症状上报事件已接收，AI 将进行分诊分析",
            data={"patient_id": event.patient_id},
            timestamp=datetime.now().isoformat(),
        )
    except Exception as e:
        log.error(f"处理症状上报事件失败: {str(e)}")
        return ApiResponse(code=500, message=f"事件处理失败: {str(e)}", data=None, timestamp=datetime.now().isoformat())


@router.post("/register-created", response_model=ApiResponse)
async def on_register_created(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    HIS 挂号创建完成 → AI 推送就诊确认信息

    HIS 在挂号记录插入后调用此接口。
    AI 将推送挂号成功的详细信息给患者。
    """
    try:
        event = RegisterCreatedEvent(**envelope.data)
        log.info(f"收到挂号创建事件 - patient_id: {event.patient_id}, register_id: {event.register_id}")
        bg.add_task(_dispatch_register_created, event)
        return ApiResponse(
            code=200,
            message="挂号创建事件已接收，AI 将通知患者",
            data={"patient_id": event.patient_id, "register_id": event.register_id},
            timestamp=datetime.now().isoformat(),
        )
    except Exception as e:
        log.error(f"处理挂号创建事件失败: {str(e)}")
        return ApiResponse(code=500, message=f"事件处理失败: {str(e)}", data=None, timestamp=datetime.now().isoformat())


@router.post("/examination-ordered", response_model=ApiResponse)
async def on_examination_ordered(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    医生开具检查项目 → AI 定时提醒患者取报告、复诊

    HIS 系统在医生开具检查单后调用此接口。
    AI 将安排延迟提醒任务，到时推送给患者。
    """
    try:
        event = ExaminationOrderedEvent(**envelope.data)
        log.info(
            f"收到检查开单事件 - patient_id: {event.patient_id}, "
            f"exam_type: {event.exam_type}"
        )

        bg.add_task(_dispatch_examination_ordered, event)

        return ApiResponse(
            code=200,
            message="检查开单事件已接收，AI 将定时提醒患者",
            data={"patient_id": event.patient_id, "exam_type": event.exam_type},
            timestamp=datetime.now().isoformat(),
        )

    except Exception as e:
        log.error(f"处理检查开单事件失败: {str(e)}")
        return ApiResponse(
            code=500,
            message=f"事件处理失败: {str(e)}",
            data=None,
            timestamp=datetime.now().isoformat(),
        )


@router.post("/report-generated", response_model=ApiResponse)
async def on_report_generated(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    检查报告生成 → AI 解读报告并提醒复诊

    HIS 系统在报告生成后调用此接口。
    AI 将立即解读报告，并将结果和复诊提醒推送给患者。
    """
    try:
        event = ReportGeneratedEvent(**envelope.data)
        log.info(
            f"收到报告生成事件 - patient_id: {event.patient_id}, "
            f"report_type: {event.report_type}"
        )

        bg.add_task(_dispatch_report_generated, event)

        return ApiResponse(
            code=200,
            message="报告生成事件已接收，AI 将解读报告并提醒患者",
            data={"patient_id": event.patient_id, "report_type": event.report_type},
            timestamp=datetime.now().isoformat(),
        )

    except Exception as e:
        log.error(f"处理报告生成事件失败: {str(e)}")
        return ApiResponse(
            code=500,
            message=f"事件处理失败: {str(e)}",
            data=None,
            timestamp=datetime.now().isoformat(),
        )


@router.post("/prescription-created", response_model=ApiResponse)
async def on_prescription_created(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    医生开具药品处方 → AI 整理用药规则、禁忌、注意事项并定时提醒用药

    HIS 系统在医生开方后调用此接口。
    AI 将分析药物相互作用，并安排定时用药提醒。
    """
    try:
        event = PrescriptionCreatedEvent(**envelope.data)
        log.info(
            f"收到处方创建事件 - patient_id: {event.patient_id}, "
            f"药品数: {len(event.medications)}"
        )

        bg.add_task(_dispatch_prescription_created, event)

        return ApiResponse(
            code=200,
            message="处方创建事件已接收，AI 将分析用药并定时提醒",
            data={
                "patient_id": event.patient_id,
                "medication_count": len(event.medications),
            },
            timestamp=datetime.now().isoformat(),
        )

    except Exception as e:
        log.error(f"处理处方创建事件失败: {str(e)}")
        return ApiResponse(
            code=500,
            message=f"事件处理失败: {str(e)}",
            data=None,
            timestamp=datetime.now().isoformat(),
        )


@router.post("/exam-completed", response_model=ApiResponse)
async def on_exam_completed(envelope: EventEnvelope, bg: BackgroundTasks):
    """
    检查结果出炉 → AI 按报告类型（数值/影像）选推送模板通知患者

    ExamResultServiceImpl 在录入首份检查结果后调用此接口。
    """
    try:
        event = ExamCompletedEvent(**envelope.data)
        log.info(
            f"收到检查结果出炉事件 - patient_id: {event.patient_id}, "
            f"item_type: {event.item_type}, inspect_name: {event.inspect_name}"
        )
        bg.add_task(_dispatch_exam_completed, event)
        return ApiResponse(
            code=200,
            message="检查结果出炉事件已接收，AI 将通知患者",
            data={"patient_id": event.patient_id, "item_type": event.item_type},
            timestamp=datetime.now().isoformat(),
        )
    except Exception as e:
        log.error(f"处理检查结果出炉事件失败: {str(e)}")
        return ApiResponse(code=500, message=f"事件处理失败: {str(e)}", data=None, timestamp=datetime.now().isoformat())


# ── 内部 dispatch 函数（由 BackgroundTasks 异步执行） ──────────


async def _dispatch_examination_ordered(event: ExaminationOrderedEvent):
    from app.core.events.event_handler import handle_examination_ordered
    await handle_examination_ordered(event)


async def _dispatch_report_generated(event: ReportGeneratedEvent):
    from app.core.events.event_handler import handle_report_generated
    await handle_report_generated(event)


async def _dispatch_prescription_created(event: PrescriptionCreatedEvent):
    from app.core.events.event_handler import handle_prescription_created
    await handle_prescription_created(event)


async def _dispatch_symptom_reported(event: PatientSymptomReportedEvent):
    from app.core.events.event_handler import handle_symptom_reported
    await handle_symptom_reported(event)


async def _dispatch_register_created(event: RegisterCreatedEvent):
    from app.core.events.event_handler import handle_register_created
    await handle_register_created(event)


async def _dispatch_exam_completed(event: ExamCompletedEvent):
    from app.core.events.event_handler import handle_exam_completed
    await handle_exam_completed(event)
