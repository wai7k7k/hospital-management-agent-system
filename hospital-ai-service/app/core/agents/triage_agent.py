"""
分诊Agent — 症状分析 + 科室推荐

基于患者症状描述，结合Pinecone医学知识库，智能推荐就诊科室并评估紧急程度。
记忆隔离: memory_id = "triage:{session_id}"
"""

from typing import Dict, Any, List
from datetime import date

from app.core.agents.base_agent import BaseAgent
from app.utils.logger import log


class TriageAgent(BaseAgent):
    """AI分诊专家"""

    AGENT_NAME = "triage"

    # ── 公开接口 ────────────────────────────────────────────

    async def analyze(self, symptoms: str, session_id: str = "default") -> Dict[str, Any]:
        """
        分析症状并推荐科室

        Returns:
            {
                "recommended_department": "神经内科",
                "alternative_department": "",
                "confidence": 0.92,
                "urgency": "普通|紧急|危急",
                "reasoning": "分诊依据",
                "suggested_questions": [...],
                "response_to_user": "..."
            }
        """
        try:
            log.info(f"[TriageAgent] 分析症状 - session: {session_id}, symptoms: {symptoms[:50]}...")

            knowledge = await self._retrieve_knowledge("triage", symptoms, top_k=3)
            knowledge_str = self._format_knowledge(knowledge, "（暂无匹配的参考知识，请基于通识推断）")

            prompt = await self._build_prompt(
                "triage",
                session_id,
                symptoms=symptoms,
                knowledge=knowledge_str,
                current_date=date.today().isoformat(),
            )

            response = await self._call_llm(prompt, max_tokens=800, temperature=0.3)
            result = self._parse_json_response(response, self._fallback())

            await self.remember(
                session_id,
                symptoms,
                result.get("response_to_user", result.get("recommended_department", "")),
            )
            return result

        except Exception as e:
            log.error(f"[TriageAgent] 分析失败: {str(e)}")
            return self._fallback()

    # ── 辅助 ────────────────────────────────────────────────

    @staticmethod
    def _format_knowledge(knowledge: List[Dict], no_data_msg: str = "") -> str:
        if not knowledge:
            return no_data_msg or "（暂无匹配的参考知识，请基于通识推断）"
        lines = []
        for i, k in enumerate(knowledge, 1):
            lines.append(
                f"{i}. 相似症状: {k.get('symptoms', '')} → 对应科室: {k.get('department', '')}，"
                f"紧急程度: {k.get('urgency', '普通')}\n"
                f"   说明: {k.get('description', '')}"
            )
        return "\n".join(lines)

    @staticmethod
    def _fallback() -> Dict[str, Any]:
        return {
            "recommended_department": "内科",
            "alternative_department": "",
            "confidence": 0.5,
            "urgency": "普通",
            "reasoning": "分析失败，建议挂内科",
            "suggested_questions": [],
            "response_to_user": "抱歉，分诊分析暂时不可用，建议您前往医院导诊台咨询。🏥",
        }
