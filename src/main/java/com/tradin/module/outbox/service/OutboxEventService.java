package com.tradin.module.outbox.service;

import com.tradin.module.futures.order.event.AutoTradeEventPublisher;
import com.tradin.module.outbox.domain.OutboxEvent;
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
    private final AutoTradeEventPublisher autoTradeEventPublisher;

    @Transactional
    @Scheduled(fixedDelay = 300)
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventReader.findAllPendingEvents();

        int batchSize = 100;
        for (int i = 0; i < pendingEvents.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, pendingEvents.size());
            List<OutboxEvent> batch = pendingEvents.subList(i, endIndex);
            autoTradeEventPublisher.publishToKafka(batch);
        }
    }
} 