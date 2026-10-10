from datetime import datetime
from app.core.events.event_models import (
    ExaminationOrderedEvent,
    ReportGeneratedEvent,
    PrescriptionCreatedEvent,
    RegisterCreatedEvent,
    PatientSymptomReportedEvent,
    ExamCompletedEvent,
    MedicationItem,
    AIResultFeedbackEvent,
)
from app.utils.logger import log
from app.utils.config import settings


# ── 检查开单事件处理 ──────────────────────────────────────────

async def handle_examination_ordered(event: ExaminationOrderedEvent):
    """
    医生开具检查项目后触发：
    1. 通知患者进行检查
    2. 安排延迟提醒取报告
    3. 安排复诊提醒
    """
    log.info(f"[事件处理] 检查开单 - patient_id: {event.patient_id}")

    # 1. 立即通知患者
    exam_info = f"{event.exam_name or event.exam_type}"
    dept = event.department_name or "相应科室"
    instructions = event.instructions or "请按医嘱准备"

    immediate_msg = (
        f"您的医生已开具【{exam_info}】检查项目。\n"
        f"请前往{dept}完成检查。\n"
        f"检查须知：{instructions}"
    )
    await _push_notification(event.patient_id, "examination_order", immediate_msg)

    # 2. 安排延迟提醒：2 小时后提醒取报告
    from app.core.services.reminder_service import get_reminder_service

    reminder = get_reminder_service()
    reminder.schedule_reminder(
        patient_id=event.patient_id,
        reminder_type="report_pickup",
        message=(
            f"提醒：您的【{exam_info}】检查报告已可领取。\n"
            f"请前往{dept}取报告，并预约复诊时间。"
        ),
        delay_seconds=7200,  # 2 小时后提醒取报告
        metadata={
            "exam_type": event.exam_type,
            "exam_id": event.exam_id,
            "doctor_id": event.doctor_id,
        },
    )

    # 3. 安排复诊提醒：24 小时后提醒
    reminder.schedule_reminder(
        patient_id=event.patient_id,
        reminder_type="follow_up",
        message=(
            f"请记得携带【{exam_info}】检查报告复诊。\n"
            f"如有不适请及时就医。"
        ),
        delay_seconds=86400,  # 24 小时
        metadata={"exam_type": event.exam_type},
    )

    # 4. 存储事件到 MongoDB
    await _store_event("examination_ordered", event.model_dump())

    # examination.ordered 是 HIS 发给 AI 的输入事件。
    # 这里不能再次发布相同 routing key，否则 AI 会消费自己刚发布的消息并形成循环。


# ── 报告生成事件处理 ──────────────────────────────────────────

async def handle_report_generated(event: ReportGeneratedEvent):
    """
    检查报告生成后触发：
    1. AI 解读报告内容
    2. 推送解读结果给患者
    3. 安排复诊提醒
    """
    log.info(f"[事件处理] 报告生成 - patient_id: {event.patient_id}")

    # 1. AI 解读报告
    interpretation = await _interpret_report(
        event.report_data,
        session_id=f"{event.patient_id}:{event.report_id or event.inspect_order_id or 'latest'}",
    )

    # 1.5 把解读结果存到 MongoDB，供用户聊天路径"报告解读"复用
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        saved = await cache.save_session_state(
            f"report_result:{event.patient_id}",
            {
                "report_name": event.report_name or event.report_type,
                "summary": interpretation.get("summary", ""),
                "abnormal_indicators": interpretation.get("abnormal_indicators", []),
                "recommendations": interpretation.get("recommendations", []),
                "cached_at": datetime.now().isoformat(),
            }
        )
        if not saved:
            raise RuntimeError(f"报告解读结果缓存失败 - patient_id: {event.patient_id}")
        log.info(f"报告解读结果已缓存 - patient_id: {event.patient_id}")
    except Exception as e:
        log.warning(f"缓存报告解读结果失败: {e}")
        raise

    # 2. 只提示报告已出，由患者确认后再通过“报告解读”读取本患者的缓存结果。
    # 避免在患者未确认时直接展示医疗解读，也让交互与真实就诊流程一致。
    report_msg = (
        f"您的【{event.report_name or event.report_type}】检查结果已经出来了。\n\n"
        f"是否需要我帮您分析这份检查结果？需要的话请回复“帮我分析报告”。"
    )

    await _push_notification(event.patient_id, "report_interpretation", report_msg)

    # 3. 安排复诊提醒
    from app.core.services.reminder_service import get_reminder_service

    reminder = get_reminder_service()
    reminder.schedule_reminder(
        patient_id=event.patient_id,
        reminder_type="follow_up_after_report",
        message=(
            f"请携带【{event.report_name or event.report_type}】报告复诊。\n"
            f"医生将根据报告结果为您制定治疗方案。"
        ),
        delay_seconds=86400,
        metadata={"report_id": event.report_id, "report_type": event.report_type},
    )

    # 4. 存储事件和 AI 结果
    await _store_event("report_generated", {
        "event": event.model_dump(),
        "ai_interpretation": interpretation,
    })

    await _publish_event("report.generated", {
        "patient_id": event.patient_id,
        "report_type": event.report_type,
        "interpretation": interpretation,
    })

    # 5. 回写 AI 解读结果到 HIS
    if event.report_id:
        from app.integrations.hospital.http_client import HospitalAPIClient
        client = HospitalAPIClient()
        try:
            await client.push_ai_result(
                biz_type="report",
                biz_id=event.report_id,
                ai_data=interpretation,
            )
        finally:
            await client.close()


# ── 处方创建事件处理 ──────────────────────────────────────────

async def handle_prescription_created(event: PrescriptionCreatedEvent):
    """
    医生开具药品处方后触发：
    1. AI 分析药物相互作用、禁忌、注意事项
    2. 推送用药指导给患者
    3. 安排定时用药提醒
    4. 回写 AI 结果到 HIS
    """
    log.info(f"[事件处理] 处方创建 - patient_id: {event.patient_id}")

    # 1. AI 药物分析
    med_names = [m.name for m in event.medications]
    med_analysis = await _analyze_medications(med_names, event.patient_id)

    # 1.5 缓存用药分析结果，供"用药咨询"聊天路径复用
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_session_state(
            f"medication_result:{event.patient_id}",
            {
                "medications": [m.model_dump() for m in event.medications],
                "analysis": med_analysis,
                "cached_at": datetime.now().isoformat(),
            }
        )
        log.info(f"用药分析结果已缓存 - patient_id: {event.patient_id}")
    except Exception as e:
        log.warning(f"缓存用药分析结果失败: {e}")

    # 2. 推送用药指导
    safe_status = "未发现明显药物相互作用" if med_analysis.get("safe") else "发现潜在药物相互作用"
    med_msg = (
        f"您的处方已开具，请前往【{settings.PHARMACY_PICKUP_LOCATION}】取药。\n"
        f"取药后请按医嘱用药。\n\n"
        f"AI 用药分析：{safe_status}\n"
    )

    if med_analysis.get("interactions"):
        med_msg += "\n药物相互作用：\n"
        for inter in med_analysis["interactions"]:
            med_msg += f"  - {inter.get('drug_a', '')} × {inter.get('drug_b', '')}: "
            med_msg += f"{inter.get('description', '')} (严重程度: {inter.get('severity', '')})\n"

    if med_analysis.get("warnings"):
        med_msg += f"\n注意事项：\n"
        for w in med_analysis["warnings"]:
            med_msg += f"  - {w}\n"

    # 用药规则整理
    med_msg += "\n用药规则：\n"
    for med in event.medications:
        med_msg += (
            f"  【{med.name}】{med.dosage}，{med.frequency}，"
            f"疗程{med.duration}\n"
        )

    if event.notes:
        med_msg += f"\n医嘱：{event.notes}"

    await _push_notification(event.patient_id, "medication_guidance", med_msg)

    # 3. 安排定时用药提醒（每 8 小时一次，共 7 天 = 21 次）
    from app.core.services.reminder_service import get_reminder_service
    reminder = get_reminder_service()
    interval_seconds = (
        settings.MEDICATION_REMINDER_INTERVAL_SECONDS
        or settings.MEDICATION_REMINDER_INTERVAL_HOURS * 3600
    )

    for med in event.medications:
        reminder.schedule_recurring_reminder(
            patient_id=event.patient_id,
            reminder_type="medication",
            message=(
                f"用药提醒：请按时服用【{med.name}】{med.dosage}。\n"
                f"用法：{med.frequency}，疗程{med.duration}。\n"
                f"{'注意事项：' + event.notes if event.notes else ''}"
            ),
            interval_seconds=interval_seconds,
            times=settings.MEDICATION_REMINDER_TIMES,
            metadata={
                "medication_name": med.name,
                "dosage": med.dosage,
                "frequency": med.frequency,
                "duration": med.duration,
            },
        )

    # 4. 存储事件
    await _store_event("prescription_created", {
        "event": event.model_dump(),
        "ai_analysis": med_analysis,
    })

    # prescription.created 是 HIS 发给 AI 的输入事件。
    # AI 的处理结果由下方回写 HIS，不能再次发布相同 routing key。

    # 5. 回写 AI 分析结果到 HIS
    if event.prescription_id:
        from app.integrations.hospital.http_client import HospitalAPIClient
        client = HospitalAPIClient()
        try:
            await client.push_ai_result(
                biz_type="prescription",
                biz_id=event.prescription_id,
                ai_data=med_analysis,
            )
        finally:
            await client.close()


# ── 患者症状上报事件处理 ──────────────────────────────────────

async def handle_symptom_reported(event: PatientSymptomReportedEvent):
    """
    患者提交症状后触发（Event 1）：
    1. AI 分诊分析（Pinecone 知识检索 + Qwen LLM）
    2. 推送分诊结果 + 推荐科室给患者
    3. 发布 ai.triage.completed 事件回 HIS
    """
    log.info(f"[事件处理] 患者症状上报 - patient_id: {event.patient_id}, symptoms: {event.symptoms[:50]}...")

    from app.core.agents import TriageAgent
    agent = TriageAgent()
    result = await agent.analyze(event.symptoms)

    dept = result.get("recommended_department", "内科")
    urgency = result.get("urgency", "普通")
    reasoning = result.get("reasoning", "")
    confidence = result.get("confidence", 0.5)

    urgency_emoji = {"危急": "🔴", "紧急": "🟡", "普通": "🟢"}.get(urgency, "🟢")

    triage_msg = (
        f"{urgency_emoji} AI 分诊结果\n\n"
        f"根据您的症状描述，AI 分析如下：\n"
        f"推荐科室：【{dept}】\n"
        f"紧急程度：{urgency}\n"
        f"置信度：{confidence:.0%}\n\n"
        f"分析依据：{reasoning}\n\n"
        f"请确认挂号意向，AI 将为您自动完成挂号。"
    )

    await _push_notification(event.patient_id, "triage_result", triage_msg)

    await _store_event("symptom_reported", {
        "event": event.model_dump(),
        "ai_triage": result,
    })

    feedback = AIResultFeedbackEvent(
        event_type="triage_completed",
        biz_type="triage",
        biz_id=event.patient_id,
        patient_id=event.patient_id,
        data=result,
    )
    await _publish_ai_feedback(feedback)


# ── 挂号创建事件处理 ──────────────────────────────────────────

async def handle_register_created(event: RegisterCreatedEvent):
    """
    HIS 挂号完成后触发（Event 2）：
    1. 确认挂号成功，推送就诊详情给患者
    2. 发布 ai.registration.confirmed 事件回 HIS
    """
    log.info(f"[事件处理] 挂号创建 - patient_id: {event.patient_id}, register_id: {event.register_id}")

    dept = event.dep_name or "相应科室"
    doctor = event.user_name or "医生"
    reg_date = event.register_date or "请查看预约详情"

    confirm_msg = (
        f"挂号成功！\n\n"
        f"就诊科室：{dept}\n"
        f"接诊医生：{doctor}\n"
        f"挂号时间：{reg_date}\n\n"
        f"请按时就诊，携带身份证和医保卡。\n"
        f"如有疑问可随时咨询 AI 助手。"
    )

    await _push_notification(event.patient_id, "registration_confirmed", confirm_msg)

    await _store_event("register_created", event.model_dump())

    feedback = AIResultFeedbackEvent(
        event_type="registration_confirmed",
        biz_type="registration",
        biz_id=str(event.register_id) if event.register_id else event.patient_id,
        patient_id=event.patient_id,
        data={"confirmed": True, "register_id": event.register_id, "patient_id": event.patient_id},
    )
    await _publish_ai_feedback(feedback)


# ── 检查结果出炉事件处理 ──────────────────────────────────────

async def handle_exam_completed(event: ExamCompletedEvent):
    """检查结果出炉后：按 NUMERIC/IMAGE_TEXT 选推送模板推送患者"""
    log.info(f"[事件处理] 检查结果出炉 - patient_id: {event.patient_id}, item_type: {event.item_type}")

    dedup_key = f"exam_push:{event.result_id}:{event.patient_id}"
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        if await cache.get_session_state(dedup_key):
            log.info(f"结果 {event.result_id} 已推送过，跳过")
            return
    except Exception:
        pass

    inspect_name = event.inspect_name or "检查"
    report_date = (event.report_time or "")[:10] if event.report_time else ""

    if event.item_type == "IMAGE_TEXT":
        conclusion = event.summary_conclusion or "报告已出具"
        push_msg = (
            f"您好，您 {report_date} 的【{inspect_name}】检查报告已出来啦。\n\n"
            f"报告简要结论：{conclusion}\n\n"
            f"需要我用通俗的话帮你解读这份检查报告内容吗？\n\n"
            f"温馨提示：AI 解读仅作参考，最终诊疗方案请遵从线下坐诊医生判断。"
        )
    else:
        ab_count = len(event.abnormal_indicators) if event.abnormal_indicators else 0
        if ab_count > 0:
            ab_items = "\n".join(
                f"  · {ab.get('indexName', '')}: {ab.get('value', '')} (参考: {ab.get('reference', '')})"
                for ab in (event.abnormal_indicators or [])[:5]
            )
            push_msg = (
                f"您好，您本次【{inspect_name}】检验报告已出具，共发现 {ab_count} 项异常指标：\n\n"
                f"{ab_items}\n\n"
                f"需要我逐条给你解释各项指标代表的身体情况吗？\n\n"
                f"温馨提示：AI 解读仅作参考，最终诊疗方案请遵从线下坐诊医生判断。"
            )
        else:
            push_msg = (
                f"您好，您本次【{inspect_name}】检验报告已出具，全部指标正常。\n\n"
                f"需要我帮你分析各项指标的含义吗？\n\n"
                f"温馨提示：AI 解读仅作参考，最终诊疗方案请遵从线下坐诊医生判断。"
            )

    await _push_notification(event.patient_id, "exam_completed", push_msg)

    # 报告出具后安排复诊提醒。测试环境可通过 .env 缩短等待时间。
    from app.core.services.reminder_service import get_reminder_service
    reminder = get_reminder_service()
    reminder.schedule_reminder(
        patient_id=event.patient_id,
        reminder_type="follow_up_after_exam",
        message=(
            f"复诊提醒：请携带【{inspect_name}】检查报告，按医嘱返回门诊复诊。\n"
            f"如出现明显不适，请及时联系医生。"
        ),
        delay_seconds=settings.FOLLOW_UP_REMINDER_DELAY_SECONDS,
        metadata={
            "result_id": event.result_id,
            "register_id": event.register_id,
            "inspect_name": inspect_name,
        },
    )

    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_session_state(dedup_key, {
            "pushed_at": datetime.now().isoformat(),
            "result_id": event.result_id,
        })
    except Exception:
        pass

    await _store_event("exam_completed", {"event": event.model_dump(), "push_msg": push_msg})


# ── 辅助函数 ──────────────────────────────────────────────────

async def _publish_ai_feedback(feedback: AIResultFeedbackEvent):
    """发布 AI 反馈事件到 RabbitMQ（ai.# 路由键）"""
    try:
        from app.integrations.mq.producer import RabbitMQProducer
        producer = RabbitMQProducer()
        try:
            routing_key = f"ai.{feedback.biz_type}.{feedback.event_type.split('_')[0]}"
            await producer.publish(
                exchange="hospital.events",
                routing_key=routing_key,
                message=feedback.model_dump(),
            )
            log.info(f"AI 反馈已发布 - routing_key: {routing_key}, biz_type: {feedback.biz_type}")
        finally:
            producer.close()
    except Exception as e:
        log.error(f"发布 AI 反馈事件失败: {str(e)}")

async def _interpret_report(report_data: str, session_id: str) -> dict:
    """调用 AI 解读报告"""
    try:
        from app.core.agents import ReportAgent
        agent = ReportAgent()
        result = await agent.interpret(report_data, session_id=session_id)
        return result
    except Exception as e:
        log.error(f"AI 报告解读失败: {str(e)}")
        return {
            "summary": "报告已生成，请咨询医生获取详细解读",
            "abnormal_indicators": [],
            "recommendations": ["请携带报告复诊"],
        }


async def _analyze_medications(medications: list, patient_id: str) -> dict:
    """调用 AI 分析药物相互作用"""
    try:
        from app.core.agents import MedicationAgent
        agent = MedicationAgent()
        result = await agent.analyze(medications, patient_id=patient_id)
        return result
    except Exception as e:
        log.error(f"AI 用药分析失败: {str(e)}")
        return {
            "safe": True,
            "interactions": [],
            "warnings": ["请按医嘱用药，如有不适及时就医"],
        }


async def _push_notification(patient_id: str, msg_type: str, message: str):
    """推送通知给患者（WebSocket + RabbitMQ + MongoDB）"""
    try:
        from app.api.v1.websocket import manager

        payload = {
            "type": "notification",
            "notification_type": msg_type,
            "patient_id": patient_id,
            "message": message,
            "timestamp": datetime.now().isoformat(),
        }

        # WebSocket 实时推送（session_id 使用 patient_id 前缀）
        await manager.send_message(f"patient:{patient_id}", payload)

    except Exception as e:
        log.error(f"WebSocket 推送失败: {str(e)}")

    # MongoDB 存储通知
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_message(
            session_id=f"patient:{patient_id}",
            role="assistant",
            content=message,
            metadata={"type": msg_type, "timestamp": datetime.now().isoformat()},
        )
    except Exception as e:
        log.error(f"MongoDB 存储通知失败: {str(e)}")


async def _store_event(event_type: str, data: dict):
    """存储事件到 MongoDB"""
    try:
        from app.utils.cache import MongoDBCache
        cache = MongoDBCache()
        await cache.save_message(
            session_id=f"events:{event_type}",
            role="system",
            content=str(data),
            metadata={
                "event_type": event_type,
                "timestamp": datetime.now().isoformat(),
            },
        )
    except Exception as e:
        log.error(f"存储事件失败: {str(e)}")


async def _publish_event(routing_key: str, data: dict):
    """发布事件到 RabbitMQ"""
    try:
        from app.integrations.mq.producer import RabbitMQProducer
        producer = RabbitMQProducer()
        try:
            await producer.publish(
                exchange="hospital.events",
                routing_key=routing_key,
                message={
                    "routing_key": routing_key,
                    "data": data,
                    "timestamp": datetime.now().isoformat(),
                },
            )
        finally:
            producer.close()
    except Exception as e:
        log.error(f"发布事件到 RabbitMQ 失败: {str(e)}")
