"""
报告解读Agent — 指标提取 + 知识检索 + AI解读

对医疗检查报告进行智能解读，识别异常指标并给出健康建议。
记忆隔离: memory_id = "report:{session_id}"

支持两种报告类型：
  - NUMERIC 化验报告：完整指标提取 + 知识检索 + AI解读
  - IMAGE_TEXT 影像报告：通俗化转述影像所见，不做虚构化验分析
"""

import json
from typing import Dict, Any, List
from datetime import date

from app.core.agents.base_agent import BaseAgent
from app.utils.logger import log


class ReportAgent(BaseAgent):
    """AI报告解读专家"""

    AGENT_NAME = "report"

    # ── 公开接口 ────────────────────────────────────────────

    async def interpret(self, report_text: str, session_id: str = "default") -> Dict[str, Any]:
        try:
            log.info(f"[ReportAgent] 解读报告 - session: {session_id}, text: {report_text[:60]}...")

            # 解析报告 JSON，判断类型
            try:
                report_obj = json.loads(report_text)
            except (json.JSONDecodeError, TypeError):
                report_obj = {"report_name": "", "item_type": "NUMERIC", "summary": str(report_text)}
            item_type = report_obj.get("item_type", "NUMERIC")

            # ── IMAGE_TEXT 影像报告：通俗转述，不做化验分析 ──
            if item_type == "IMAGE_TEXT":
                return await self._interpret_image(report_obj)

            # ── NUMERIC 化验报告：完整流水线 ──
            indicators = await self._extract_indicators(report_text)

            knowledge_str = "（暂无匹配的参考知识库数据）"
            try:
                knowledge = await self._retrieve_knowledge_for_indicators(indicators)
                knowledge_str = self._format_knowledge(knowledge)
            except Exception as e:
                log.warning(f"[ReportAgent] 知识检索跳过: {e}")

            try:
                prompt = await self._build_prompt(
                    "report", session_id,
                    report_data=report_text,
                    knowledge=knowledge_str,
                    current_date=date.today().isoformat(),
                )
            except Exception as e:
                log.warning(f"[ReportAgent] 记忆加载跳过: {e}")
                from app.core.prompts import render
                prompt = render("report", report_data=report_text, knowledge=knowledge_str,
                                current_date=date.today().isoformat())

            response = await self._call_llm(prompt, max_tokens=1500, temperature=0.3)
            result = self._parse_json_response(response, self._fallback())

            try:
                await self.remember(session_id, report_text[:200], result.get("summary", ""))
            except Exception:
                pass
            return result

        except Exception as e:
            log.error(f"[ReportAgent] 解读失败: {str(e)}")
            return self._fallback()

    # ── 影像报告解读 ──────────────────────────────────────────

    async def _interpret_image(self, report_obj: dict) -> Dict[str, Any]:
        report_name = report_obj.get("report_name", "影像检查")
        check_content = report_obj.get("check_content", "")
        check_conclusion = report_obj.get("check_conclusion", "")
        summary = "\n".join(
            part for part in [check_content, check_conclusion, report_obj.get("summary", "")]
            if part and str(part).strip()
        )

        if not summary or not summary.strip():
            return {
                "summary": f"您的【{report_name}】影像报告已出具，请咨询医生获取详细解读。",
                "abnormal_indicators": [],
                "recommendations": ["请携带报告复诊，由临床医生进行专业解读。"],
                "response_to_user": f"您的【{report_name}】影像报告已出具。影像报告需要临床医生结合查体和其他检查综合判断，建议您携带报告复诊。",
            }

        prompt = (
            f"你是医学报告转述助手，只做一件事：把影像报告的检查所见用通俗语言解释给患者听。\n\n"
            f"## 本次报告信息\n"
            f"- 报告名称：【{report_name}】\n"
            f"- 报告类型：影像报告（不涉及任何化验指标）\n\n"
            f"## 严格规则（违反任何一条都是错误）\n"
            f"1. 只转述下面「检查所见」中实际提到的内容\n"
            f"2. 禁止提及肝功能、肾功能、血脂、血糖、血常规、尿酸等任何化验指标\n"
            f"3. 禁止虚构数值和参考范围\n"
            f"4. 如果检查所见为空或无法解读，请诚实告知患者需要咨询医生\n\n"
            f"## 检查所见\n"
            f"{summary}\n\n"
            f"## 输出格式（纯JSON，不要markdown代码块）\n"
            f'{{"summary": "通俗解读（2-3句，必须提及报告名称）", '
            f'"abnormal_indicators": [], '
            f'"recommendations": ["建议1", "建议2"], '
            f'"response_to_user": "给患者的完整回复"}}'
        )

        response = await self._call_llm(prompt, max_tokens=1000, temperature=0.3)
        result = self._parse_json_response(response, {
            "summary": summary[:200] if summary else f"您的【{report_name}】影像报告已出具。",
            "abnormal_indicators": [],
            "recommendations": ["请携带报告复诊，由临床医生进行专业解读。"],
            "response_to_user": f"您的【{report_name}】影像报告已出具，请携带报告复诊。",
        })
        return result

    # ── 指标提取 ────────────────────────────────────────────

    async def _extract_indicators(self, report_text: str) -> List[Dict]:
        """从报告原文中提取结构化指标"""
        try:
            response = await self._call_llm(
                prompt=(
                    f"从以下医疗报告中提取所有检测指标。以 JSON 格式返回:\n"
                    f'{{"indicators": [{{"name": "指标名", "value": "检测值", "unit": "单位", '
                    f'"reference_range": "参考范围", "is_abnormal": true/false}}]}}\n\n'
                    f"报告内容:\n{report_text}"
                ),
                max_tokens=1000,
                temperature=0.1,
            )
            data = self._parse_json_response(response, {"indicators": []})
            return data.get("indicators", [])
        except Exception as e:
            log.error(f"[ReportAgent] 指标提取失败: {str(e)}")
            return []

    async def _retrieve_knowledge_for_indicators(self, indicators: List[Dict]) -> List[Dict]:
        """为异常指标检索知识"""
        knowledge = []
        try:
            for ind in indicators:
                if ind.get("is_abnormal"):
                    results = await self._retrieve_knowledge(
                        "report", ind.get("name", ""), top_k=1
                    )
                    knowledge.extend(results)
            log.info(f"[ReportAgent] 检索到 {len(knowledge)} 条报告知识")
        except Exception as e:
            log.error(f"[ReportAgent] 知识检索失败: {str(e)}")
        return knowledge

    # ── 辅助 ────────────────────────────────────────────────

    @staticmethod
    def _format_knowledge(knowledge: List[Dict], no_data_msg: str = "") -> str:
        if not knowledge:
            return no_data_msg or "（暂无匹配的参考知识库数据）"
        lines = []
        for i, k in enumerate(knowledge, 1):
            lines.append(
                f"{i}. 指标: {k.get('name', k.get('item', ''))}\n"
                f"   临床意义: {k.get('description', k.get('clinical_significance', ''))}\n"
                f"   参考范围: {k.get('reference_range', '')}"
            )
        return "\n".join(lines)

    @staticmethod
    def _fallback() -> Dict[str, Any]:
        return {
            "summary": "报告解读暂时不可用，请稍后重试或咨询医生。",
            "abnormal_indicators": [],
            "recommendations": ["请携带报告原件就医"],
        }
