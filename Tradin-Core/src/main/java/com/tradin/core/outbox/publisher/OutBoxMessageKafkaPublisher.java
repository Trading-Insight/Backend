package com.tradin.core.outbox.publisher;


import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
import com.tradin.core.outbox.implement.dto.OutBoxMessagesEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxMessageKafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxMessageProcessor outboxMessageProcessor;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishToKafka(OutBoxMessagesEvent event) {
        List<OutboxMessage> outboxMessages = event.getOutboxMessages();
        outboxMessageProcessor.markAllAsPublished(outboxMessages);

        List<OutboxMessage> failedMessages = new ArrayList<>();

        //TODO
        for (OutboxMessage message : outboxMessages) {
            try {
                kafkaTemplate.send(message.getMessageType().getTopic(), message.getMessageId(), message.getPayload());
            } catch (Exception e) {
                message.markAsPublishingFailed(e.getMessage());
                failedMessages.add(message);
            }
        }

        outboxMessageProcessor.markAllAsPublishingFailed(failedMessages);
    }
}