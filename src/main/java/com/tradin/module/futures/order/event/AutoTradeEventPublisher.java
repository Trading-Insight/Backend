package com.tradin.module.futures.order.event;

import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.implement.OutboxEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTradeEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final OutboxEventProcessor outboxEventProcessor;

    @Async("autoTradeExecutor") //TODO - 스레드풀 관리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishToKafka(OutboxEvent event) {
        try {
            kafkaTemplate.send(event.getEventType().getTopic(), event.getEventId(), event.getPayload());
            outboxEventProcessor.markAsPublished(event);
        } catch (Exception e) {
            outboxEventProcessor.markAsFailed(event, e.getMessage());
        }
    }
} 