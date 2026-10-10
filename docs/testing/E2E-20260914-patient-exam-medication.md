# 患者检查与用药通知端到端测试

## 测试数据

- 日期：2026-09-14 上午
- 患者：测试患者
- 身份证：TEST_CARD_001
- 手机号：TEST_PHONE_001
- 测试补充字段：女、1990-01-01、初始密码按本地配置
- 科室：眼科
- 医生账号：yan
- 接诊人数：30
- 取药地点：门诊一楼药房

## 启动条件

同时运行 Nacos、Redis、RabbitMQ、MongoDB、五个 Java 核心服务、前端和 Hospital AI Service。不要启动 `RuoYiStreamMqApplication`。

在 AI 助手的患者选择器中先选择“测试患者”。选中后 WebSocket 会连接到 `patient:{patientId}`，后续业务事件才会实时显示在该聊天窗口。

当前本地 `.env` 使用测试时间：检查结果生成 20 秒后提醒复诊；开药后每 20 秒提醒一次，共两次。

## 操作与验收

1. 在患者建档页新建患者：姓名测试患者、身份证 TEST_CARD_001、手机 TEST_PHONE_001、性别女、生日 1990-01-01、密码按本地配置。
2. 在排班管理新增排班：2026-09-14、上午、眼科、医生 yan、接诊人数 30。
3. 在患者自助挂号页输入身份证，筛选上述排班并提交挂号。排班余量应从 30 变为 29，AI 日志应出现 `挂号创建`，聊天窗口应收到挂号成功通知。
4. 在医生就诊页用身份证查询测试患者，点击“开检查单”，选择一个检查项目并提交。AI 日志应出现 `检查开单`，聊天窗口应收到检查通知。
5. 在检查结果管理选择测试患者的申请单，录入数值指标或影像文本结果并提交。Java 日志应出现 `检查结果出炉事件已发布`，AI 日志应出现 `检查结果出炉`。聊天窗口应收到“报告已出来，是否需要分析”的通知。
6. 在 AI 聊天窗口回复“需要，请帮我分析”。应返回本次检查结果的通俗解读。结果生成约 20 秒后，应收到复诊提醒。
7. 回到医生就诊页，查询同一挂号并点击“开处方”，选择药品、填写诊断和备注后提交。Java 日志应出现 `处方创建事件已发布`，AI 日志应出现 `处方创建`。
8. 聊天窗口应立即收到取药地点和用药指导；约 20 秒、40 秒分别收到两次用药提醒。

## 通过标准

- patient、schedule、register、inspect_order、exam_result、prescription 数据均能按同一 patientId/registerId 串联。
- RabbitMQ 依次传递 register.created、examination.ordered、exam.completed、prescription.created。
- AI 助手实时收到挂号、检查、检查结果、复诊、取药和用药提醒。
- 消息不重复，检查结果与处方属于测试患者，排班余量正确扣减。

## 测试后恢复

将 `hospital-ai-service/.env` 中的测试值恢复为生产节奏：

```dotenv
FOLLOW_UP_REMINDER_DELAY_SECONDS=86400
MEDICATION_REMINDER_INTERVAL_SECONDS=28800
MEDICATION_REMINDER_TIMES=21
```
