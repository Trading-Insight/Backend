package com.tradin.core.outbox.service;


import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.event.OutBoxMessagePublisher;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
import com.tradin.core.outbox.implement.OutboxMessageReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxMessageService {

    private final OutboxMessageProcessor outboxMessageProcessor;
    private final OutboxMessageReader outboxMessageReader;
    private final OutBoxMessagePublisher outBoxMessagePublisher;

    @Transactional
    @Scheduled(fixedDelay = 100)
    public void processOutboxMessages() {
        List<OutboxMessage> pendingMessages = outboxMessageReader.findAllPendingMessages();

        if (pendingMessages.isEmpty()) {
            return;
        }
        outBoxMessagePublisher.publishToKafka(pendingMessages);
    }
} 