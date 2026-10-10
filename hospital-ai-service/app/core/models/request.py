from typing import Optional, List, Dict, Any
from pydantic import BaseModel, Field
from datetime import datetime
from enum import Enum


class IntentType(str, Enum):
    """意图类型"""
    TRIAGE = "triage"           # 分诊
    REGISTER = "register"       # 挂号
    REPORT = "report"           # 报告解读
    MEDICATION = "medication"   # 用药提醒
    HOSPITAL = "hospital"       # 医院信息
    CHAT = "chat"               # 普通聊天


class ChatMessage(BaseModel):
    """聊天消息"""
    role: str = Field(..., description="角色: user/assistant/system")
    content: str = Field(..., description="消息内容")
    timestamp: datetime = Field(default_factory=datetime.now)


class ChatRequest(BaseModel):
    """聊天请求"""
    session_id: str = Field(..., description="会话ID")
    message: str = Field(..., description="用户消息")
    user_id: Optional[str] = Field(None, description="用户ID")
    patient_id: Optional[str] = Field(None, description="患者ID")


class ChatResponse(BaseModel):
    """聊天响应"""
    session_id: str
    message: str
    intent: Optional[str] = None
    confidence: Optional[float] = None
    metadata: Optional[Dict[str, Any]] = None


class TriageRequest(BaseModel):
    """分诊请求"""
    symptoms: str = Field(..., description="症状描述")
    duration: Optional[str] = Field(None, description="持续时间")
    severity: Optional[str] = Field(None, description="严重程度: mild/moderate/severe")


class TriageResponse(BaseModel):
    """分诊响应"""
    recommended_department: str
    confidence: float
    reasoning: str
    urgency: str


class RegisterRequest(BaseModel):
    """挂号请求"""
    patient_id: str
    department_id: str
    doctor_id: Optional[str] = None
    appointment_date: str
    time_slot: str


class RegisterResponse(BaseModel):
    """挂号响应"""
    success: bool
    appointment_id: Optional[str] = None
    message: str


class ReportInterpretationRequest(BaseModel):
    """报告解读请求"""
    report_type: str
    indicators: Dict[str, Any]


class ReportInterpretationResponse(BaseModel):
    """报告解读响应"""
    summary: str
    abnormal_indicators: List[Dict[str, Any]]
    recommendations: List[str]


class MedicationCheckRequest(BaseModel):
    """用药检查请求"""
    medications: List[str]
    patient_id: Optional[str] = None


class MedicationCheckResponse(BaseModel):
    """用药检查响应"""
    safe: bool
    interactions: List[Dict[str, Any]]
    warnings: List[str]


class DiagnosisState(BaseModel):
    """诊断状态 - LangGraph 状态定义"""
    session_id: str
    messages: List[ChatMessage]
    intent: Optional[IntentType] = None
    confidence: Optional[float] = None
    symptoms: Optional[str] = None
    patient_id: Optional[str] = None
    user_id: Optional[str] = None
    triage_result: Optional[TriageResponse] = None
    register_result: Optional[RegisterResponse] = None
    report_result: Optional[ReportInterpretationResponse] = None
    medication_result: Optional[MedicationCheckResponse] = None
    error: Optional[str] = None
    response: Optional[str] = None
    metadata: Dict[str, Any] = Field(default_factory=dict)
