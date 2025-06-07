package com.tradin.module.outbox.service;

import com.tradin.module.futures.order.event.AutoTradeEventPublisher;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.implement.OutboxEventReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventReader outboxEventReader;
    private final AutoTradeEventPublisher autoTradeEventPublisher;
    
    @Scheduled(fixedDelay = 1000)
    public void processOutboxEvents() { //TODO - 분산 시스템으로 확장 시 락 필요
        List<OutboxEvent> pendingEvents = outboxEventReader.findAllPendingEvents();

        for (OutboxEvent event : pendingEvents) {
            autoTradeEventPublisher.publishToKafka(event);
        }
    }
} 