"""
用药安全Agent — 药物相互作用分析 + 用药注意事项

分析处方药品清单，检测药物间相互作用，提供用药安全警告。
记忆隔离: memory_id = "medication:{session_id}"
"""

from typing import Dict, Any, List
from datetime import date

from app.core.agents.base_agent import BaseAgent
from app.utils.logger import log


class MedicationAgent(BaseAgent):
    """AI用药安全顾问"""

    AGENT_NAME = "medication"

    # ── 公开接口 ────────────────────────────────────────────

    async def analyze(
        self,
        medications: List[str],
        patient_id: str = None,
        session_id: str = "default",
    ) -> Dict[str, Any]:
        """
        分析药物相互作用

        Args:
            medications: 药品名称列表
            patient_id: 患者ID（可选）
            session_id: 会话ID

        Returns:
            {"safe": bool, "interactions": [...], "warnings": [...], "response_to_user": "..."}
        """
        try:
            log.info(f"[MedicationAgent] 分析用药 - meds: {medications}, session: {session_id}")

            knowledge = await self._retrieve_interactions(medications)
            knowledge_str = self._format_knowledge(knowledge)
            patient_context = f"患者ID: {patient_id}" if patient_id else "未提供患者信息"

            prompt = await self._build_prompt(
                "medication",
                session_id,
                medications="、".join(medications),
                patient_context=patient_context,
                knowledge=knowledge_str,
                current_date=date.today().isoformat(),
            )

            response = await self._call_llm(prompt, max_tokens=1200, temperature=0.3)
            result = self._parse_json_response(response, self._fallback())

            await self.remember(
                session_id,
                f"药品清单: {', '.join(medications)}",
                result.get("response_to_user", result.get("overall_rating", "")),
            )
            return result

        except Exception as e:
            log.error(f"[MedicationAgent] 分析失败: {str(e)}")
            return self._fallback()

    # ── 知识检索 ────────────────────────────────────────────

    async def _retrieve_interactions(self, medications: List[str]) -> List[Dict]:
        """检索药物两两之间的相互作用"""
        interactions = []
        try:
            for i in range(len(medications)):
                for j in range(i + 1, len(medications)):
                    query = f"{medications[i]} {medications[j]} 相互作用"
                    results = await self._retrieve_knowledge("medication", query, top_k=2)
                    interactions.extend(results)
            log.info(f"[MedicationAgent] 检索到 {len(interactions)} 条药物相互作用")
        except Exception as e:
            log.error(f"[MedicationAgent] 药物知识检索失败: {str(e)}")
        return interactions

    @staticmethod
    def _format_knowledge(knowledge: List[Dict], no_data_msg: str = "") -> str:
        if not knowledge:
            return no_data_msg or "（暂无已知的药物相互作用数据）"
        return "\n".join(
            f"- {k.get('drug_a', '')} × {k.get('drug_b', '')}: "
            f"{k.get('description', '')} (严重程度: {k.get('severity', '未知')})"
            for k in knowledge
        )

    # ── 辅助 ────────────────────────────────────────────────

    @staticmethod
    def _fallback() -> Dict[str, Any]:
        return {
            "safe": False,
            "interactions": [],
            "warnings": ["用药分析暂时不可用，请咨询药师或医生。"],
            "response_to_user": "",
        }
