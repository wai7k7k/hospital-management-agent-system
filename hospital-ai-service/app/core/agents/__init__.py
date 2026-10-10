"""
Agent层 — LangChain Agent模式的实现

架构:
  BaseAgent        → 公共能力（LLM调用、知识检索、记忆管理、Prompt渲染、响应解析）
  TriageAgent      → 症状分析 + 科室推荐
  ReportAgent      → 报告指标提取 + AI解读
  MedicationAgent  → 药物相互作用分析 + 用药警告
  HospitalAgent    → 科室/医生/排班查询 + 医院指南

每个Agent拥有独立的记忆隔离（memory_id = "{agent_name}:{session_id}"）
Services层通过调用Agent来编排完整的业务流程。
"""

from app.core.agents.base_agent import BaseAgent
from app.core.agents.triage_agent import TriageAgent
from app.core.agents.report_agent import ReportAgent
from app.core.agents.medication_agent import MedicationAgent
from app.core.agents.hospital_agent import HospitalAgent
from app.core.agents.register_agent import RegisterAgent

__all__ = [
    "BaseAgent",
    "TriageAgent",
    "ReportAgent",
    "MedicationAgent",
    "HospitalAgent",
    "RegisterAgent",
]
