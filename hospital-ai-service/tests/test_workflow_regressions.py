import asyncio
import tempfile
import unittest
from pathlib import Path
from unittest.mock import AsyncMock, Mock, patch

from app.core.agents.register_agent import RegisterAgent
from app.core.services.register_service import RegisterService
from app.core.services.intent_service import IntentService
from app.integrations.mq.consumer import ManagedConsumerManager
from app.utils.instance_lock import single_instance


class WorkflowRegressions(unittest.IsolatedAsyncioTestCase):
    async def test_explicit_business_intents_do_not_depend_on_llm(self):
        service = IntentService()
        cases = {
            "我想挂号，请帮我预约一个科室": "register",
            "要挂号": "register",
            "今天眼睛不舒服": "triage",
            "帮我分析检查结果": "report",
            "这个药怎么吃": "medication",
        }
        with patch("app.core.services.intent_service.Generation.call") as llm_call:
            for message, expected in cases.items():
                result = await service.recognize_intent(message)
                self.assertEqual(result["intent"].value, expected)
                self.assertEqual(result["metadata"]["source"], "local_rules")
            llm_call.assert_not_called()

    async def test_confirmation_keeps_collected_schedule(self):
        info = {"_schedule_id": "123", "department": "眼科"}
        service = RegisterService()
        service._load_state = AsyncMock(return_value={"step": "confirm_register", "collected_info": info})
        service._save_state = AsyncMock()
        service._agent = Mock()
        service._agent.process = AsyncMock(return_value={
            "step": "done", "collected_info": info, "response": "挂号成功",
            "register_result": {"success": True, "appointment_id": "456"},
        })
        result = await service.process_register_request("test", "确认挂号")
        self.assertTrue(result.success)
        self.assertEqual(service._agent.process.call_args.kwargs["step"], "confirm_register")
        self.assertEqual(service._agent.process.call_args.kwargs["collected_info"], info)

    async def test_active_confirmation_bypasses_llm(self):
        from app.core.graph.nodes import intent_recognition_node
        from app.core.models.request import ChatMessage, IntentType
        state = {"session_id": "test", "messages": [ChatMessage(role="user", content="确认")], "metadata": {}}
        with patch("app.core.graph.nodes._check_active_register", AsyncMock(return_value=True)), patch(
            "app.core.services.intent_service.IntentService"
        ) as service:
            result = await intent_recognition_node(state)
            self.assertEqual(result["intent"], IntentType.REGISTER)
            service.return_value.recognize_intent.assert_not_called()

    async def test_cancel_is_not_confirmation(self):
        self.assertFalse(RegisterAgent._is_confirm("确认取消"))
        self.assertFalse(RegisterAgent._is_confirm("不确认"))
        self.assertTrue(RegisterAgent._is_confirm("确认挂号"))

    async def test_confirmation_recovers_missing_schedule_id(self):
        agent = RegisterAgent()
        collected = {
            "patient_name": "测试患者",
            "card_id": "TEST_CARD_001",
            "department": "眼科",
            "date": "2026-09-15",
            "time_slot": "上午",
        }
        schedule = {
            "scheduleId": "2097695596629995521",
            "userId": "1562376351004221441",
            "userName": "yan",
        }
        agent._query_schedules = AsyncMock(return_value=[schedule])
        agent._execute_his_register = AsyncMock(return_value={
            "success": True,
            "appointment_id": schedule["scheduleId"],
            "doctor": "yan",
            "fee": "¥5.00",
        })

        result = await agent._handle_confirm_register(collected, "确认", "patient-1")

        self.assertEqual(result["step"], "done")
        self.assertEqual(result["collected_info"]["_schedule_id"], schedule["scheduleId"])
        submitted = agent._execute_his_register.call_args.args[0]
        self.assertEqual(submitted["_doctor_name"], "yan")

    async def test_confirmation_with_multiple_schedules_returns_to_doctor_choice(self):
        agent = RegisterAgent()
        collected = {
            "patient_name": "测试患者",
            "card_id": "TEST_CARD_001",
            "department": "眼科",
            "date": "2026-09-15",
            "time_slot": "上午",
        }
        agent._query_schedules = AsyncMock(return_value=[
            {"scheduleId": "1", "userId": "11", "userName": "yan", "scheduleNum": 30},
            {"scheduleId": "2", "userId": "12", "userName": "李医生", "scheduleNum": 20},
        ])
        agent._execute_his_register = AsyncMock()

        result = await agent._handle_confirm_register(collected, "确认", "patient-1")

        self.assertEqual(result["step"], "choose_doctor")
        agent._execute_his_register.assert_not_called()

    async def test_failed_handler_is_not_marked_processed(self):
        manager = ManagedConsumerManager()
        manager._loop = asyncio.get_running_loop()
        manager._event_exists = Mock(return_value=False)
        manager._mark_processed = Mock(return_value=True)
        manager._handle_report_completed = AsyncMock(side_effect=RuntimeError("database unavailable"))
        try:
            message = {"eventId": "test", "data": {"patientId": "patient-1"}}
            ok = await asyncio.to_thread(manager._dispatch, "report.completed", message)
            self.assertFalse(ok)
            manager._mark_processed.assert_not_called()
        finally:
            manager.shutdown()

    async def test_patient_event_without_patient_id_is_acknowledged_and_skipped(self):
        manager = ManagedConsumerManager()
        manager._loop = asyncio.get_running_loop()
        manager._handle_examination_ordered = AsyncMock()
        manager._mark_processed = Mock(return_value=True)
        try:
            ok = await asyncio.to_thread(
                manager._dispatch,
                "examination.ordered",
                {"eventId": "unknown", "data": {"inspectName": "眼压检查"}},
            )
            self.assertTrue(ok)
            manager._handle_examination_ordered.assert_not_called()
            manager._mark_processed.assert_not_called()
        finally:
            manager.shutdown()

    async def test_shutdown_rejects_new_event(self):
        manager = ManagedConsumerManager()
        manager.shutdown()
        self.assertFalse(manager._dispatch("report.completed", {"eventId": "test"}))

    async def test_single_instance_releases_lock(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / "service.lock"
            with single_instance(path):
                with self.assertRaises(RuntimeError):
                    with single_instance(path):
                        pass
            with single_instance(path):
                pass


if __name__ == "__main__":
    unittest.main()
