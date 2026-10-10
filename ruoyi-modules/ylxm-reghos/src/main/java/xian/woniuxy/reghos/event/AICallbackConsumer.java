package xian.woniuxy.reghos.event;

import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * AI 结果回写消费者
 * 监听 ai.result.feedback.queue，接收 AI 分析结果并回写 HIS 数据库
 */
@Slf4j
@Component
public class AICallbackConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "ai.result.feedback.queue")
    public void handleAIResult(Message message, Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            String body = new String(message.getBody(), "UTF-8");
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(body, Map.class);

            String bizType = (String) payload.getOrDefault("bizType", (String) payload.get("biz_type"));
            String bizId = (String) payload.getOrDefault("bizId", (String) payload.get("biz_id"));
            String eventType = (String) payload.getOrDefault("eventType", (String) payload.get("event_type"));

            log.info("收到 AI 反馈 - bizType: {}, bizId: {}, eventType: {}", bizType, bizId, eventType);

            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) payload.getOrDefault("data", payload);

            switch (bizType != null ? bizType : "") {
                case "triage":
                    handleTriageFeedback(bizId, data);
                    break;
                case "registration":
                    handleRegistrationFeedback(bizId, data);
                    break;
                case "report":
                    handleReportFeedback(bizId, data);
                    break;
                case "prescription":
                    handlePrescriptionFeedback(bizId, data);
                    break;
                default:
                    log.warn("未知 AI 反馈业务类型: {}", bizType);
            }

            channel.basicAck(deliveryTag, false);
            log.info("AI 反馈处理完成 - bizType: {}, bizId: {}", bizType, bizId);

        } catch (Exception e) {
            log.error("处理 AI 反馈失败: {}", e.getMessage(), e);
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                log.error("nack 失败: {}", ex.getMessage());
            }
        }
    }

    /**
     * 处理分诊反馈 — AI 推荐的科室和诊断结果
     */
    private void handleTriageFeedback(String bizId, Map<String, Object> data) {
        String dept = (String) data.getOrDefault("recommended_department", "未知");
        String urgency = (String) data.getOrDefault("urgency", "普通");
        log.info("[分诊反馈] patientId: {}, 推荐科室: {}, 紧急程度: {}", bizId, dept, urgency);
        // 后续可扩展：写入患者推荐记录表
    }

    /**
     * 处理挂号确认反馈
     */
    private void handleRegistrationFeedback(String bizId, Map<String, Object> data) {
        Boolean confirmed = (Boolean) data.getOrDefault("confirmed", false);
        log.info("[挂号确认] registerId: {}, confirmed: {}", bizId, confirmed);
        // 后续可扩展：更新挂号记录 remark
    }

    /**
     * 处理报告解读反馈
     */
    private void handleReportFeedback(String bizId, Map<String, Object> data) {
        String summary = (String) data.getOrDefault("summary", "");
        log.info("[报告解读] reportId: {}, summary: {}", bizId, summary != null ? summary.substring(0, Math.min(summary.length(), 100)) : "");
        // 后续可扩展：将 AI 解读写入检查订单 remark
    }

    /**
     * 处理处方用药分析反馈
     */
    private void handlePrescriptionFeedback(String bizId, Map<String, Object> data) {
        Boolean safe = (Boolean) data.getOrDefault("safe", true);
        log.info("[用药分析] prescriptionId: {}, safe: {}", bizId, safe);
        // 后续可扩展：将用药分析写入处方 remark
    }
}
