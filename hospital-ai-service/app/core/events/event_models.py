from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
from enum import Enum


class EventType(str, Enum):
    PATIENT_SYMPTOM_REPORTED = "patient_symptom_reported"
    REGISTER_CREATED = "register_created"
    EXAMINATION_ORDERED = "examination_ordered"
    REPORT_GENERATED = "report_generated"
    PRESCRIPTION_CREATED = "prescription_created"


class MedicationItem(BaseModel):
    name: str = Field(..., description="药品名称")
    dosage: str = Field("", description="剂量")
    frequency: str = Field("", description="用药频率")
    duration: str = Field("", description="用药时长")
    notes: Optional[str] = Field(None, description="备注")


# ── HIS 业务事件模型 ──────────────────────────────────────────

class ExaminationOrderedEvent(BaseModel):
    """医生开具检查项目事件"""
    inspect_order_id: Optional[int] = Field(None, description="检查订单ID")
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    exam_id: Optional[str] = Field(None, description="检查项目ID")
    exam_type: str = Field(..., description="检查类型，如 blood_test, ct, xray")
    exam_name: str = Field("", description="检查项目名称")
    doctor_id: Optional[str] = Field(None, description="开单医生ID")
    doctor_name: Optional[str] = Field(None, description="开单医生姓名")
    department_name: Optional[str] = Field(None, description="执行科室")
    order_time: Optional[str] = Field(None, description="开单时间")
    register_id: Optional[int] = Field(None, description="关联挂号ID")
    instructions: Optional[str] = Field(None, description="检查须知")
    estimated_report_time: Optional[str] = Field(None, description="预计出报告时间")


class ReportGeneratedEvent(BaseModel):
    """检查报告生成事件"""
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    report_id: Optional[str] = Field(None, description="报告ID")
    report_type: str = Field(..., description="报告类型: blood_routine, blood_glucose, lfts")
    report_name: Optional[str] = Field(None, description="报告名称")
    report_data: str = Field("", description="报告原始内容/指标数据")
    indicators: Optional[List[Dict[str, Any]]] = Field(default_factory=list, description="指标数据列表（Java端是List）")
    exam_id: Optional[str] = Field(None, description="关联检查项目ID")
    inspect_order_id: Optional[int] = Field(None, description="关联检查订单ID")
    register_id: Optional[int] = Field(None, description="关联挂号ID")
    doctor_id: Optional[str] = Field(None, description="报告医生ID")
    generated_time: Optional[str] = Field(None, description="报告生成时间")
    is_abnormal: Optional[bool] = Field(None, description="是否有异常指标")


class ExamCompletedEvent(BaseModel):
    """检查结果出炉事件（来自 ExamResultServiceImpl）"""
    result_id: Optional[int] = Field(None, description="exam_result.id")
    apply_id: Optional[int] = Field(None, description="inspect_order_id")
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    register_id: Optional[int] = Field(None, description="挂号ID")
    inspect_name: Optional[str] = Field(None, description="检查项目名称")
    item_type: str = Field("NUMERIC", description="NUMERIC数值指标 / IMAGE_TEXT影像文本")
    summary_conclusion: Optional[str] = Field(None, description="报告结论")
    doctor_id: Optional[int] = Field(None, description="出报告医生ID")
    report_time: Optional[str] = Field(None, description="报告出具时间")
    abnormal_indicators: List[Dict[str, Any]] = Field(default_factory=list, description="异常指标列表")
    check_content: Optional[str] = Field(None, description="影像文本-检查所见全文")
    check_conclusion: Optional[str] = Field(None, description="影像文本-检查结论")


class PrescriptionCreatedEvent(BaseModel):
    """医生开具药品处方事件"""
    prescription_id: Optional[str] = Field(None, description="处方ID")
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    register_id: Optional[int] = Field(None, description="关联挂号ID")
    record_id: Optional[int] = Field(None, description="关联病历ID")
    dep_id: Optional[int] = Field(None, description="科室ID")
    dep_name: Optional[str] = Field(None, description="科室名称")
    medications: List[MedicationItem] = Field(default_factory=list, description="药品列表")
    doctor_id: Optional[str] = Field(None, description="开方医生ID")
    doctor_name: Optional[str] = Field(None, description="开方医生姓名")
    diagnosis: Optional[str] = Field(None, description="诊断结果")
    prescription_date: Optional[str] = Field(None, description="开方日期")
    prescribed_time: Optional[str] = Field(None, description="开方时间")
    notes: Optional[str] = Field(None, description="医嘱备注")


class RegisterCreatedEvent(BaseModel):
    """挂号创建事件 — HIS 推送挂号完成后触发"""
    register_id: Optional[int] = Field(None, description="挂号ID")
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    dep_id: Optional[int] = Field(None, description="科室ID")
    dep_name: Optional[str] = Field(None, description="科室名称")
    user_id: Optional[int] = Field(None, description="医生ID")
    user_name: Optional[str] = Field(None, description="医生姓名")
    register_date: Optional[str] = Field(None, description="挂号日期")
    register_price: Optional[float] = Field(None, description="挂号费用")


class PatientSymptomReportedEvent(BaseModel):
    """患者症状上报事件"""
    patient_id: str = Field(..., description="患者ID")
    patient_name: Optional[str] = Field(None, description="患者姓名")
    symptoms: str = Field(..., description="症状描述")
    session_id: Optional[str] = Field(None, description="会话ID")
    urgency_self_assessment: Optional[str] = Field(None, description="自评紧急程度")


# ── AI 反馈事件模型 ──────────────────────────────────────────

class AIResultFeedbackEvent(BaseModel):
    """AI 分析结果回写 HIS 的事件模型"""
    event_id: str = Field(default_factory=lambda: datetime.now().strftime("%Y%m%d%H%M%S%f"))
    event_type: str = Field(..., description="事件类型: triage_completed, report_interpreted, medication_analyzed, registration_confirmed")
    source: str = Field(default="ai-service")
    timestamp: str = Field(default_factory=lambda: datetime.now().isoformat())
    biz_type: str = Field(..., description="业务类型: triage, report, prescription, registration")
    biz_id: str = Field(..., description="业务主键ID")
    patient_id: str = Field(..., description="患者ID")
    data: dict = Field(..., description="AI 分析结果数据")


# ── 通用事件信封 ──────────────────────────────────────────────

class EventEnvelope(BaseModel):
    """HIS 系统推送事件的通用外层包装"""
    event_id: str = Field(default_factory=lambda: datetime.now().strftime("%Y%m%d%H%M%S%f"))
    event_type: EventType = Field(..., description="事件类型")
    timestamp: str = Field(default_factory=lambda: datetime.now().isoformat())
    source: str = Field(default="his-system", description="事件来源")
    service_id: Optional[str] = Field(None, description="来源微服务: ylxm-reghos, ylxm-customer")
    data: dict = Field(..., description="事件负载数据")


# ── 提醒记录模型 ──────────────────────────────────────────────

class ReminderRecord(BaseModel):
    patient_id: str
    reminder_type: str
    message: str
    scheduled_at: str
    sent_at: Optional[str] = None
    delivered: bool = False
    channel: str = "websocket"
