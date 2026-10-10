"""
医院信息Agent — 科室查询 + 医生信息 + 排班查询 + 医院指南

提供医院基本信息查询服务，可作为LangGraph中 hospital_info_node 的后端。
记忆隔离: memory_id = "hospital:{session_id}"
"""

from typing import Dict, Any, List, Optional
from datetime import date

from app.core.agents.base_agent import BaseAgent
from app.utils.logger import log
from app.integrations.hospital.http_client import HospitalAPIClient


class HospitalAgent(BaseAgent):
    """AI医院信息顾问"""

    AGENT_NAME = "hospital"

    def __init__(self):
        super().__init__()
        self._his: Optional[HospitalAPIClient] = None

    @property
    def his(self) -> HospitalAPIClient:
        if self._his is None:
            self._his = HospitalAPIClient()
        return self._his

    # ── 公开接口 ────────────────────────────────────────────

    async def query_department(self, department_name: str) -> Optional[Dict[str, Any]]:
        """查询科室信息（先查HIS，找不到再用LLM推荐）"""
        result = await self.his.get_department(department_name)
        if result:
            return result

        log.info(f"[HospitalAgent] HIS未找到科室 '{department_name}'，启用LLM推断")
        try:
            prompt = (
                f"患者想查找科室: {department_name}\n"
                f"请根据你的医学知识，给出最接近的标准科室名称。\n"
                f"返回JSON: {{\"department_name\": \"标准科室名称\", \"description\": \"科室介绍\"}}\n"
                f"当前日期: {date.today().isoformat()}"
            )
            response = await self._call_llm(prompt, max_tokens=300, temperature=0.3)
            return self._parse_json_response(response)
        except Exception as e:
            log.error(f"[HospitalAgent] LLM科室推断失败: {str(e)}")
            return None

    async def query_doctors(
        self, department_name: str, date_str: str = ""
    ) -> Dict[str, Any]:
        """查询某科室的医生排班信息"""
        try:
            dept = await self.his.get_department(department_name)
            if not dept:
                return {"department": department_name, "doctors": [], "message": "未找到该科室"}

            prompt = (
                f"患者查询科室 '{department_name}' 的医生排班信息。\n"
                f"请生成该科室的典型医生介绍和就诊建议。\n"
                f"返回JSON: {{\"department\": \"...\", \"description\": \"...\", "
                f"\"typical_doctors\": [{{\"name\": \"...\", \"title\": \"...\", \"specialty\": \"...\"}}], "
                f"\"visit_tips\": \"...\", \"response_to_user\": \"...\"}}\n"
                f"当前日期: {date.today().isoformat()}"
            )
            response = await self._call_llm(prompt, max_tokens=600, temperature=0.5)
            return self._parse_json_response(response, {"department": department_name, "doctors": []})
        except Exception as e:
            log.error(f"[HospitalAgent] 医生查询失败: {str(e)}")
            return {"department": department_name, "doctors": [], "message": str(e)}

    async def query_hospital_info(self, query: str, session_id: str = "default") -> str:
        """
        回答医院相关的一般性问题（地址、电话、科室分布、就诊流程等）

        Args:
            query: 用户查询
            session_id: 会话ID

        Returns:
            回复文本
        """
        try:
            prompt = await self._build_prompt(
                template_name=None,  # 不使用预设模板
                session_id=session_id,
            )
            # 手动构建prompt
            from app.core.prompts import render
            from app.core.memory import get_memory_provider

            memory = await self._load_memory(session_id)
            context = memory.to_prompt_context()

            full_prompt = (
                f"你是北京协和医院的智能导诊助手。请友好、简洁地回答患者的医院相关问题。\n"
                f"当前日期: {date.today().isoformat()}\n\n"
                f"对话历史:\n{context}\n\n"
                f"患者: {query}\n\n"
                f"请回复（30-80字）："
            )

            response = await self._call_llm(full_prompt, max_tokens=300, temperature=0.7)
            reply = (
                response.output.text
                if response.status_code == 200
                else "您好！有什么关于医院的问题我可以帮您解答吗？"
            )
            await self.remember(session_id, query, reply)
            return reply

        except Exception as e:
            log.error(f"[HospitalAgent] 医院信息查询失败: {str(e)}")
            return "抱歉，暂时无法查询医院信息，请稍后重试。"

    async def close(self):
        if self._his:
            await self._his.close()
