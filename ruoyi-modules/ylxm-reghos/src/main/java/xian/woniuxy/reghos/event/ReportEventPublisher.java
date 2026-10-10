package xian.woniuxy.reghos.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 报告完成事件发布器（共享组件）
 * BloodRoutine / BloodGlucose / Lfts 三个 Service 共用此组件发布报告完成事件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReportEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "hospital.events";
    private static final String ROUTING_KEY = "report.completed";

    /**
     * 发布报告完成事件到 RabbitMQ
     *
     * @param reportType      报告类型: blood_routine, blood_glucose, lfts
     * @param reportName      报告中文名
     * @param reportId        报告主键ID
     * @param patientId       患者ID
     * @param patientName     患者姓名
     * @param registerId      关联挂号ID
     * @param indicators      指标数据 Map（key=指标名, value=检测值）
     * @param referenceRanges 参考范围 Map（key=指标名, value=参考范围）
     */
    public void publishReportCompleted(
            String reportType,
            String reportName,
            Long reportId,
            Long patientId,
            String patientName,
            Long registerId,
            Map<String, String> indicators,
            Map<String, String> referenceRanges) {

        try {
            boolean hasAbnormal = checkAbnormal(indicators, referenceRanges);

            Map<String, Object> data = new HashMap<>();
            data.put("reportId", reportId);
            data.put("patientId", patientId);
            data.put("patientName", patientName);
            data.put("reportType", reportType);
            data.put("reportName", reportName);
            data.put("registerId", registerId);
            data.put("indicators", buildIndicatorMap(indicators, referenceRanges));
            data.put("isAbnormal", hasAbnormal);
            data.put("generatedTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            Map<String, Object> envelope = new HashMap<>();
            envelope.put("eventId", reportType + "-" + reportId + "-" + System.currentTimeMillis());
            envelope.put("eventType", "REPORT_COMPLETED");
            envelope.put("source", "his-system");
            envelope.put("serviceId", "ylxm-reghos");
            envelope.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            envelope.put("data", data);

            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, envelope);
            log.info("报告完成事件已发布 - reportType: {}, reportId: {}, patientId: {}",
                    reportType, reportId, patientId);

        } catch (Exception e) {
            log.error("发布报告完成事件失败: {}", e.getMessage(), e);
        }
    }

    private Map<String, String> buildIndicatorMap(Map<String, String> indicators, Map<String, String> references) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : indicators.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String reference = references != null ? references.getOrDefault(key, "") : "";
            String combined = String.format("%s (参考值: %s)", value, reference.isEmpty() ? "无" : reference);
            result.put(key, combined);
        }
        return result;
    }

    private boolean checkAbnormal(Map<String, String> indicators, Map<String, String> references) {
        if (indicators == null || references == null) {
            return false;
        }
        for (Map.Entry<String, String> entry : indicators.entrySet()) {
            String key = entry.getKey();
            String refRange = references.get(key);
            if (refRange != null && !refRange.isEmpty()) {
                try {
                    double value = Double.parseDouble(entry.getValue());
                    String[] parts = refRange.split("-");
                    if (parts.length == 2) {
                        double low = Double.parseDouble(parts[0].trim());
                        double high = Double.parseDouble(parts[1].trim());
                        if (value < low || value > high) {
                            return true;
                        }
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return false;
    }
}
