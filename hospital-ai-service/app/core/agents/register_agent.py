"""
挂号Agent — 多轮对话状态机

流程:
  collect_info → choose_doctor → confirm_register → done
  收集信息 → 选医生 → 确认 → 挂号执行
"""

import json
from typing import Dict, Any, Optional
from datetime import date

from dashscope import Generation
from app.utils.config import settings
from app.utils.logger import log

REQUIRED_FIELDS = ["patient_name", "card_id", "department", "date", "time_slot"]

FIELD_PROMPTS = {
    "patient_name": "请输入您的真实姓名",
    "card_id": "请输入您的18位身份证号码",
    "department": "请输入您想预约的科室（如：内科、神经内科、心内科等）",
    "date": "请输入您希望的就诊日期（格式：2026-06-15）",
    "time_slot": "请选择就诊时段：上午 或 下午",
}

SCHEDULE_PROMPTS = {
    "choose_doctor": "请选择您想预约的医生（输入医生姓名即可）",
    "no_schedule": "该科室当前时段暂无可选医生排班，将为您直接挂号（医生到院分配）。",
}


class RegisterAgent:
    """挂号向导 — 选医生 → 确认 → 挂号"""

    AGENT_NAME = "register"

    def __init__(self):
        self.api_key = settings.DASH_SCOPE_API_KEY
        self.model = settings.QWEN_MODEL

    # ── 主入口 ─────────────────────────────────────────────

    async def process(
        self,
        step: str,
        collected_info: Dict[str, str],
        user_message: str,
        patient_id: Optional[str] = None,
    ) -> Dict[str, Any]:
        msg = user_message.strip()

        # ── 统一的挂号意图检测（语义匹配，支持任意语序）────
        _is_register_intent = ("挂号" in msg or "预约" in msg or
                               "帮我挂" in msg or "我想挂" in msg or "我要挂" in msg)

        # 任何非 collect_info 状态 + 用户表达挂号意图 → 重置
        if step == "done" and _is_register_intent and "成功" not in msg:
            log.info(f"[RegisterAgent] step={step} 检测到挂号意图，重置到 collect_info")
            return {
                "step": "collect_info",
                "collected_info": {},
                "response": "好的，我来帮您挂号。\n\n" + FIELD_PROMPTS["department"] + "\n\n" + self._format_collected({}),
                "register_result": None,
            }

        # ── done 状态：尝试从消息中提取挂号参数 ──
        if step == "done":
            extracted = await self._extract_fields(msg)
            has_params = any(extracted.get(f) and extracted[f].strip()
                            for f in ["department", "date", "time_slot"])
            if has_params:
                log.info(f"[RegisterAgent] done状态检测到挂号参数: {extracted}，自动进入挂号流程")
                updated = dict(collected_info)
                # 保留已有参数，只覆盖消息中新提取到的
                for f in REQUIRED_FIELDS:
                    if extracted.get(f) and extracted[f].strip():
                        updated[f] = extracted[f].strip()
                # 预填患者信息（如果有 patient_id）
                if patient_id and not updated.get("patient_name"):
                    try:
                        from app.integrations.hospital.http_client import HospitalAPIClient
                        client = HospitalAPIClient()
                        try:
                            info = await client.get_patient_info(int(patient_id))
                            if info:
                                if not updated.get("patient_name"):
                                    updated["patient_name"] = info.get("patientName", "")
                                if not updated.get("card_id"):
                                    updated["card_id"] = info.get("patientCardId", "")
                        finally:
                            await client.close()
                    except Exception:
                        pass
                # 如果已集齐全部字段 → 直接查排班
                if not [f for f in REQUIRED_FIELDS if not updated.get(f)]:
                    schedules = await self._query_schedules(updated)
                    if schedules:
                        return self._show_schedule_choices(updated, schedules)
                    else:
                        return {
                            "step": "collect_info",
                            "collected_info": updated,
                            "response": (
                                f"⚠️ {updated['department']} {updated['date']} {updated['time_slot']} 暂无医生排班。\n\n"
                                f"请更换日期、时段或科室后重试。"
                            ),
                            "register_result": None,
                        }
                # 未集齐 → 回到收集阶段继续追问
                missing = [f for f in REQUIRED_FIELDS if not updated.get(f)]
                all_fields = []
                for f in REQUIRED_FIELDS:
                    label = {"patient_name":"姓名","card_id":"身份证号","department":"科室","date":"日期","time_slot":"时段"}[f]
                    if updated.get(f):
                        val = updated[f]
                        if f == "card_id":
                            val = "****" + val[-4:]
                        all_fields.append(f"  ✅ {label}: {val}")
                    else:
                        all_fields.append(f"  ⬜ {FIELD_PROMPTS[f]}")
                return {
                    "step": "collect_info",
                    "collected_info": updated,
                    "response": f"好的，挂号需要以下信息：\n\n" + "\n".join(all_fields) + f"\n\n请告诉我 {FIELD_PROMPTS[missing[0]]}",
                    "register_result": None,
                }
            # 无挂号意图也无参数 → 交回上游
            return {
                "step": "done", "collected_info": collected_info,
                "response": None,
                "register_result": None,
            }

        if step == "collect_info":
            return await self._handle_collect_info(collected_info, msg, patient_id)

        elif step == "choose_doctor":
            return await self._handle_choose_doctor(collected_info, msg, patient_id)

        elif step == "confirm_register":
            return await self._handle_confirm_register(collected_info, msg, patient_id)

        elif step == "confirm_cancel":
            return await self._handle_confirm_cancel(collected_info, msg)

        return await self._handle_collect_info(collected_info, msg, patient_id)

    # ── 阶段1：信息收集 → 查排班 → 选医生 ──────────────────

    async def _handle_collect_info(
        self, collected: Dict[str, str], msg: str, patient_id: Optional[str],
    ) -> Dict[str, Any]:
        # LLM 提取用户消息中的字段
        extracted = await self._extract_fields(msg)
        updated = dict(collected)

        # 首次进入 + 有 patient_id → 调 HIS 预填患者档案信息（姓名、身份证）
        if patient_id and not any(updated.get(f) for f in REQUIRED_FIELDS):
            try:
                from app.integrations.hospital.http_client import HospitalAPIClient
                client = HospitalAPIClient()
                try:
                    patient_info = await client.get_patient_info(int(patient_id))
                    if patient_info:
                        pname = patient_info.get("patientName", "")
                        pcard = patient_info.get("patientCardId", "")
                        if pname and not updated.get("patient_name"):
                            updated["patient_name"] = pname
                            log.info(f"[RegisterAgent] 预填患者姓名: {pname}")
                        if pcard and not updated.get("card_id"):
                            updated["card_id"] = pcard
                            log.info(f"[RegisterAgent] 预填身份证号: ****{pcard[-4:]}")
                finally:
                    await client.close()
            except Exception as e:
                log.warning(f"[RegisterAgent] 预填患者信息失败: {e}")

        # 合并 LLM 提取的字段
        for field in REQUIRED_FIELDS:
            if extracted.get(field) and extracted[field].strip():
                updated[field] = extracted[field].strip()

        missing = [f for f in REQUIRED_FIELDS if not updated.get(f)]

        if missing:
            # 一次性列出所有待填字段 + 已填状态
            all_fields = []
            for f in REQUIRED_FIELDS:
                if updated.get(f):
                    label = {
                        "patient_name": "姓名", "card_id": "身份证号", "department": "科室",
                        "date": "日期", "time_slot": "时段"
                    }[f]
                    val = updated[f]
                    if f == "card_id":
                        val = "****" + val[-4:]
                    all_fields.append(f"  ✅ {label}: {val}")
                else:
                    all_fields.append(f"  ⬜ {FIELD_PROMPTS[f]}")
            missing_prompts = "\n".join(all_fields)
            return {
                "step": "collect_info", "collected_info": updated,
                "response": f"好的，挂号需要以下信息，请逐一提供：\n\n{missing_prompts}\n\n请告诉我 {FIELD_PROMPTS[missing[0]]}",
                "register_result": None,
            }

        # 信息齐全 → 先查排班，再决定下一步
        schedules = await self._query_schedules(updated)
        if schedules:
            return self._show_schedule_choices(updated, schedules)
        else:
            # 无排班时没有可提交的 scheduleId，继续留在信息修改阶段。
            return {
                "step": "collect_info",
                "collected_info": updated,
                "response": (
                    f"⚠️ {updated['department']} {updated['date']} {updated['time_slot']} 暂无医生排班。\n\n"
                    f"可能原因：\n"
                    f"1. 该时段确实无排班 → 建议换日期或时段\n"
                    f"2. 排班服务暂时不可用 → 请稍后重试\n\n"
                    f"请直接发送新的日期、时段或科室。"
                ),
                "register_result": None,
            }

    # ── 阶段2：选医生 ──────────────────────────────────────

    async def _handle_choose_doctor(
        self, collected: Dict[str, str], msg: str, patient_id: Optional[str],
    ) -> Dict[str, Any]:
        """让患者从排班列表中选择医生（按姓名匹配）"""
        import json as _json
        raw = collected.get("_schedules", "[]")
        try:
            schedules = _json.loads(raw) if isinstance(raw, str) else raw
        except Exception:
            schedules = []

        # 匹配医生姓名
        matched = None
        for s in schedules:
            doctor = s.get("userName", "")
            if doctor and doctor in msg:
                matched = s
                break

        if matched:
            updated = dict(collected)
            updated["_schedule_id"] = str(matched.get("scheduleId", ""))
            updated["_doctor_name"] = matched.get("userName", "")
            updated["_doctor_id"] = str(matched.get("userId", ""))
            return {
                "step": "confirm_register",
                "collected_info": updated,
                "response": (
                    f"已选择医生：{matched['userName']}\n\n"
                    + self._build_confirm_message(updated)
                ),
                "register_result": None,
            }

        # 没匹配到 → 重新展示
        return self._show_schedule_choices(collected, schedules)

    # ── 阶段3：确认后执行挂号 ──────────────────────────────

    async def _handle_confirm_register(
        self, collected: Dict[str, str], msg: str, patient_id: Optional[str],
    ) -> Dict[str, Any]:
        """确认阶段：确认→执行guahao / 取消 / 修改"""
        if self._is_confirm(msg):
            effective_collected = dict(collected)

            # 兼容修复前保存的旧会话：旧流程可能在没有 scheduleId 时进入确认阶段。
            # 确认时重新查排班；唯一排班直接绑定，多个排班则请患者选择医生。
            if not effective_collected.get("_schedule_id"):
                schedules = await self._query_schedules(effective_collected)
                if not schedules:
                    return {
                        "step": "collect_info",
                        "collected_info": effective_collected,
                        "response": (
                            f"⚠️ 目前仍未查到 {effective_collected.get('department', '')} "
                            f"{effective_collected.get('date', '')} "
                            f"{effective_collected.get('time_slot', '')} 的有效排班。\n\n"
                            f"请更换日期、时段或科室后重试。"
                        ),
                        "register_result": None,
                    }
                if len(schedules) > 1:
                    return self._show_schedule_choices(effective_collected, schedules)

                selected = schedules[0]
                effective_collected["_schedule_id"] = str(selected.get("scheduleId", ""))
                effective_collected["_doctor_name"] = selected.get("userName", "")
                effective_collected["_doctor_id"] = str(selected.get("userId", ""))

            result = await self._execute_his_register(effective_collected, patient_id)
            if result and result.get("success"):
                d = result.get("doctor", effective_collected.get("_doctor_name", ""))
                return {
                    "step": "done", "collected_info": effective_collected,
                    "response": (
                        f"✅ 挂号成功！\n\n"
                        f"就诊科室：{effective_collected['department']}\n"
                        + (f"接诊医生：{d}\n" if d else "")
                        + f"就诊日期：{effective_collected['date']} {effective_collected['time_slot']}\n"
                        f"挂号费用：{result.get('fee', '¥5.00')}\n"
                        f"预约号：{result.get('appointment_id', '请到院取号')}\n\n"
                        f"⚠️ 请按时就诊，携带身份证和医保卡。"
                        + (f"\n（系统提示：{result['warning']}）" if result.get("warning") else "")
                    ),
                    "register_result": result,
                }
            return {
                "step": "done", "collected_info": effective_collected,
                "response": (
                    f"❌ 挂号提交失败：{result.get('message', '系统暂时不可用') if result else 'HIS系统暂时不可用'}\n\n"
                    f"建议：换个时间段重试，或前往医院窗口办理。如需重新挂号，请发送「我要挂号」。"
                ),
                "register_result": result or {"success": False, "message": "HIS系统连接失败"},
            }

        elif self._is_cancel(msg):
            return {
                "step": "confirm_cancel", "collected_info": collected,
                "response": f"⚠️ 确定要取消本次挂号吗？\n\n{self._format_collected(collected)}\n\n回复「确认取消」放弃挂号，或回复「继续挂号」保留。",
                "register_result": None,
            }

        else:
            extracted = await self._extract_fields(msg)
            updated = dict(collected)
            has_update = False
            for field in REQUIRED_FIELDS:
                if extracted.get(field) and extracted[field].strip():
                    if updated.get(field) != extracted[field].strip():
                        updated[field] = extracted[field].strip()
                        has_update = True
            if has_update:
                return {
                    "step": "confirm_register", "collected_info": updated,
                    "response": "已更新信息，请再次确认：\n\n" + self._build_confirm_message(updated),
                    "register_result": None,
                }
            return {
                "step": "confirm_register", "collected_info": collected,
                "response": f"请明确确认或取消。\n回复「确认」提交挂号，或「取消」放弃。\n\n{self._format_collected(collected)}",
                "register_result": None,
            }

    # ── 取消确认 ───────────────────────────────────────────

    async def _handle_confirm_cancel(
        self, collected: Dict[str, str], msg: str,
    ) -> Dict[str, Any]:
        if self._is_confirm(msg):
            return {
                "step": "done", "collected_info": collected,
                "response": "🔄 挂号已取消。如需重新挂号，请随时发送「我要挂号」。",
                "register_result": None,
            }
        elif self._is_cancel(msg):
            return {
                "step": "collect_info", "collected_info": {},
                "response": "已清除所有填写信息。如需重新挂号，请发送「我要挂号」。",
                "register_result": None,
            }
        else:
            return {
                "step": "confirm_register", "collected_info": collected,
                "response": f"请明确回复：\n「确认取消」放弃挂号\n「继续挂号」回到确认页面\n\n{self._format_collected(collected)}",
                "register_result": None,
            }

    # ── 查排班 ─────────────────────────────────────────────

    async def _query_schedules(self, collected: Dict[str, str]) -> list:
        """
        查护士同款排班 —— 按科室名称查 ruoyi-system /schedule/list，
        绕过 reghos dep 表和 system dept 表 depId 不对应的问题。
        """
        try:
            from app.integrations.hospital.http_client import HospitalAPIClient
            client = HospitalAPIClient()
            try:
                schedules = await client.get_system_schedule_list(
                    dep_name=collected["department"],
                    date_str=collected["date"],
                    time_slot=collected["time_slot"],
                )
                if schedules:
                    log.info(f"[RegisterAgent] 系统排班表返回 {len(schedules)} 条")
                    return schedules
                log.warning(f"[RegisterAgent] 系统排班表为空 — depName={collected['department']}, date={collected['date']}, timeSlot={collected['time_slot']}")
                return []
            finally:
                await client.close()
        except Exception as e:
            log.warning(f"[RegisterAgent] 查排班失败: {e}")
            return []

    def _show_schedule_choices(self, collected: Dict[str, str], schedules: list) -> dict:
        lines = []
        for s in schedules[:5]:
            doctor = s.get("userName", "未知医生")
            num = s.get("scheduleNum", 0) or 0
            sy = s.get("shengyu")  # 系统排班接口不计算剩余，可能为 None
            if sy and int(sy) > 0:
                lines.append(f"  · {doctor}（号源 {sy}/{num}）")
            else:
                lines.append(f"  · {doctor}（号源 {num} 位）")
        updated = dict(collected)
        updated["_schedules"] = json.dumps(schedules, ensure_ascii=False)
        return {
            "step": "choose_doctor",
            "collected_info": updated,
            "response": (
                f"📅 {collected['department']} {collected['date']} {collected['time_slot']} 以下医生可约：\n\n"
                + "\n".join(lines)
                + "\n\n请输入您想预约的医生姓名。"
            ),
            "register_result": None,
        }

    # ── HIS挂号执行 ────────────────────────────────────────

    async def _execute_his_register(
        self, collected: Dict[str, str], patient_id: Optional[str],
    ) -> Optional[Dict[str, Any]]:
        """
        统一走 GuahaoController.guahao(scheduleId, patientCardId) 路径，
        和前端挂号完全一致：号源校验 → 防重复 → 写死5元 → Redis 全量缓存。
        """
        if not collected.get("_schedule_id"):
            return {"success": False, "message": "尚未选择有效排班，请先选择日期和医生。"}
        try:
            from app.integrations.hospital.http_client import HospitalAPIClient
            client = HospitalAPIClient()
            try:
                card_id = collected.get("card_id", "")
                sid_str = collected.get("_schedule_id", "")
                if sid_str:
                    result = await client.do_guahao(int(sid_str), card_id)
                    msg_val = result.get("message", "") if isinstance(result, dict) else str(result)
                    if msg_val == "ok":
                        log.info(f"[RegisterAgent] guahao 成功, scheduleId={sid_str}, cardId=****{card_id[-4:]}")
                        return {
                            "success": True,
                            "appointment_id": str(sid_str),
                            "department": collected.get("department", ""),
                            "doctor": collected.get("_doctor_name", ""),
                            "date": collected["date"],
                            "time_slot": collected["time_slot"],
                            "fee": "¥5.00",
                        }
                    code_map = {
                        "no": "该时段号源已满，请选择其他时段或医生。",
                        "exist": "您已有在途挂号记录，请勿重复挂号。",
                        "noinfo": "未找到患者档案信息，请确认身份证号正确。",
                        "noschedule": "该排班信息不存在，请重新选择。",
                    }
                    return {"success": False, "message": code_map.get(msg_val, f"挂号失败：{msg_val}")}
            finally:
                await client.close()
        except Exception as e:
            log.error(f"[RegisterAgent] HIS挂号失败: {e}")
            return {"success": False, "message": f"HIS系统连接失败：{str(e)}"}

    # ── LLM字段提取 ────────────────────────────────────────

    async def _extract_fields(self, msg: str) -> Dict[str, str]:
        try:
            today = date.today().isoformat()
            response = Generation.call(
                api_key=self.api_key, model=self.model,
                prompt=(
                    f"参考日期：今天是{today}。\n从用户消息中提取挂号信息。只提取明确出现的，不推测。\n"
                    f'返回纯JSON：{{"patient_name":"","card_id":"","department":"","date":"","time_slot":""}}\n'
                    f"日期非YYYY-MM-DD请转换。相对日期请基于今天计算。\n\n"
                    f"用户消息: {msg}"
                ),
                max_tokens=300, temperature=0.1,
            )
            if response.status_code == 200:
                content = response.output.text
                s = content.find("{"); e = content.rfind("}") + 1
                if s != -1 and e > s:
                    return json.loads(content[s:e])
        except Exception as e:
            log.error(f"[RegisterAgent] 字段提取失败: {e}")
        return {}

    # ── 辅助方法 ───────────────────────────────────────────

    def _ask_next_field(self, step: str, collected: Dict[str, str]) -> Dict[str, Any]:
        missing = [f for f in REQUIRED_FIELDS if not collected.get(f)]
        if not missing:
            return {"step": "confirm_register", "collected_info": collected,
                    "response": self._build_confirm_message(collected), "register_result": None}
        return {"step": step, "collected_info": collected,
                "response": f"好的，我来帮您挂号。\n\n{FIELD_PROMPTS[missing[0]]}\n\n{self._format_collected(collected)}",
                "register_result": None}

    @staticmethod
    def _build_confirm_message(info: Dict[str, str]) -> str:
        doctor_line = ""
        if info.get("_doctor_name"):
            doctor_line = f"  接诊医生：{info['_doctor_name']}\n"
        return (
            f"📋 请确认您的挂号信息：\n\n"
            f"  姓名：{info.get('patient_name', '')}\n"
            f"  身份证号：****{info.get('card_id', '')[-4:]}\n"
            f"  预约科室：{info.get('department', '')}\n"
            f"{doctor_line}"
            f"  预约日期：{info.get('date', '')}\n"
            f"  预约时段：{info.get('time_slot', '')}\n\n"
            f"以上信息无误吗？\n✅ 回复「确认」提交挂号\n❌ 回复「取消」放弃挂号"
        )

    @staticmethod
    def _format_collected(info: Dict[str, str]) -> str:
        parts = []
        labels = {"patient_name": "姓名", "card_id": "身份证号", "department": "科室",
                   "date": "日期", "time_slot": "时段"}
        for field, label in labels.items():
            val = info.get(field, "")
            if val:
                parts.append(f"✅ {label}: {val if field != 'card_id' else '****'+val[-4:]}")
            else:
                parts.append(f"⬜ {label}: 待填写")
        return "\n".join(parts)

    @staticmethod
    def _is_confirm(msg: str) -> bool:
        return msg.strip().rstrip("。！! ") in [
            "确认", "是的", "确定", "可以", "好的", "没问题", "对", "没错",
            "确认挂号", "提交", "继续挂号", "继续", "行", "嗯", "好"
        ]

    @staticmethod
    def _is_cancel(msg: str) -> bool:
        return any(kw in msg for kw in ["取消", "不要", "算了", "不了", "放弃", "取消挂号", "不挂"])
