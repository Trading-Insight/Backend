package com.tradin.module.outbox.service;

import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.event.OutBoxEventPublisher;
import com.tradin.module.outbox.implement.OutboxEventProcessor;
import com.tradin.module.outbox.implement.OutboxEventReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventProcessor outboxEventProcessor;
    private final OutboxEventReader outboxEventReader;
    private final OutBoxEventPublisher outBoxEventPublisher;

    @Transactional
    @Scheduled(fixedDelay = 100)
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventReader.findAllPendingEvents();

        if (pendingEvents.isEmpty()) {
            return;
        }

        outBoxEventPublisher.publishToKafka(pendingEvents);
    }
} 