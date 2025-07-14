package com.tradin.module.outbox.event;

import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.implement.OutboxEventProcessor;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventProcessor outboxEventProcessor;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishToKafka(List<OutboxEvent> outboxEvents) {
        List<OutboxEvent> successEvents = new ArrayList<>();
        List<OutboxEvent> failedEvents = new ArrayList<>();

        //TODO
        for (OutboxEvent event : outboxEvents) {
            try {
                kafkaTemplate.send(event.getEventType().getTopic(), null, event.getPayload());
                successEvents.add(event);
            } catch (Exception e) {
                event.markAsPublishingFailed(e.getMessage());
                failedEvents.add(event);
            }
        }

        //TODO
        outboxEventProcessor.markAllAsPublished(successEvents);
        outboxEventProcessor.markAllAsPublishingFailed(failedEvents);
    }
} 