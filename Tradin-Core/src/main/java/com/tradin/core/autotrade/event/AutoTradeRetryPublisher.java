package com.tradin.core.autotrade.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.outbox.domain.OutboxMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTradeRetryPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String RETRY_TOPIC = "auto-trade-retry-topic";

    public void publishToRetryTopic(OutboxMessage outboxMessage) {
        try {
            String message = outboxMessage.getPayload();
            kafkaTemplate.send(RETRY_TOPIC, message);
        } catch (Exception e) {
            log.error("재처리 토픽 전송 실패: eventId={}, topic={}, error={}", 
                outboxMessage.getMessageId(), RETRY_TOPIC, e.getMessage(), e);
        }
    }
} 