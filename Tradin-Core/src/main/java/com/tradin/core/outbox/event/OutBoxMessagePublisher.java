package com.tradin.core.outbox.event;


import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
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
public class OutBoxMessagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxMessageProcessor outboxMessageProcessor;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishToKafka(List<OutboxMessage> outboxMessages) {
        List<OutboxMessage> successMessages = new ArrayList<>();
        List<OutboxMessage> failedMessages = new ArrayList<>();

        //TODO
        for (OutboxMessage message : outboxMessages) {
            try {
                kafkaTemplate.send(message.getMessageType().getTopic(), message.getMessageId(), message.getPayload());
                successMessages.add(message);
            } catch (Exception e) {
                message.markAsPublishingFailed(e.getMessage());
                failedMessages.add(message);
            }
        }

        //TODO
        outboxMessageProcessor.markAllAsPublished(successMessages);
        outboxMessageProcessor.markAllAsPublishingFailed(failedMessages);
    }
} 