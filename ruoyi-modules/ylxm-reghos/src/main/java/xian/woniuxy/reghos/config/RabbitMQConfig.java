package xian.woniuxy.reghos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 队列/交换机/绑定声明
 * 确保所有业务队列在服务启动时自动创建
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "hospital.events";

    /**
     * 全局 JSON 消息转换器 —— 所有 convertAndSend 都发 JSON，
     * Python AI 服务收到的是标准 UTF-8 JSON 而不是 Java 序列化字节。
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ── 交换机 ──────────────────────────────────────────────

    @Bean
    public TopicExchange hospitalEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    // ── 入站队列（HIS → AI） ────────────────────────────────

    @Bean
    public Queue symptomReportedQueue() {
        return QueueBuilder.durable("symptom.reported.queue").build();
    }

    @Bean
    public Queue registerCreatedQueue() {
        return QueueBuilder.durable("register.created.queue").build();
    }

    @Bean
    public Queue inspectionOrderedQueue() {
        return QueueBuilder.durable("inspection.ordered.queue").build();
    }

    @Bean
    public Queue reportCompletedQueue() {
        return QueueBuilder.durable("report.completed.queue").build();
    }

    @Bean
    public Queue prescriptionCreatedQueue() {
        return QueueBuilder.durable("prescription.created.queue").build();
    }

    // ── 出站队列（AI → HIS） ────────────────────────────────

    @Bean
    public Queue aiResultFeedbackQueue() {
        return QueueBuilder.durable("ai.result.feedback.queue").build();
    }

    @Bean
    public Queue reminderNotificationQueue() {
        return QueueBuilder.durable("reminder.notification.queue").build();
    }

    // ── 绑定 ───────────────────────────────────────────────

    @Bean
    public Binding symptomReportedBinding() {
        return BindingBuilder.bind(symptomReportedQueue())
                .to(hospitalEventsExchange()).with("patient.symptom.reported");
    }

    @Bean
    public Binding registerCreatedBinding() {
        return BindingBuilder.bind(registerCreatedQueue())
                .to(hospitalEventsExchange()).with("register.created");
    }

    @Bean
    public Binding inspectionOrderedBinding() {
        return BindingBuilder.bind(inspectionOrderedQueue())
                .to(hospitalEventsExchange()).with("examination.ordered");
    }

    @Bean
    public Binding reportCompletedBinding() {
        return BindingBuilder.bind(reportCompletedQueue())
                .to(hospitalEventsExchange()).with("report.completed");
    }

    @Bean
    public Binding examCompletedBinding() {
        return BindingBuilder.bind(reportCompletedQueue())
                .to(hospitalEventsExchange()).with("exam.completed");
    }

    @Bean
    public Binding prescriptionCreatedBinding() {
        return BindingBuilder.bind(prescriptionCreatedQueue())
                .to(hospitalEventsExchange()).with("prescription.created");
    }

    @Bean
    public Binding aiResultFeedbackBinding() {
        return BindingBuilder.bind(aiResultFeedbackQueue())
                .to(hospitalEventsExchange()).with("ai.#");
    }

    @Bean
    public Binding reminderNotificationBinding() {
        return BindingBuilder.bind(reminderNotificationQueue())
                .to(hospitalEventsExchange()).with("reminder.*");
    }
}
