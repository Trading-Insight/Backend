package com.tradin.module.outbox.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.dto.AutoTradeEventDto;
import com.tradin.module.futures.order.event.dto.PositionDto;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxEventType;
import com.tradin.module.outbox.domain.repository.OutboxEventRepository;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Qualifier("outboxExecutor")
    private final Executor outboxExecutor;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAutoTradingEvents(Strategy strategy, List<Account> accounts, PositionDto position) {
        List<CompletableFuture<OutboxEvent>> futures = accounts.stream()
            .map(account -> CompletableFuture.supplyAsync(
                () -> createOutboxEvent(strategy, account, position),
                outboxExecutor
            ))
            .toList();

        List<OutboxEvent> events = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        outboxEventRepository.saveAll(events);
    }


    public void publishAutoTradingEvent(Strategy strategy, Account account, PositionDto position) {
        String eventId = UUID.randomUUID().toString();

        AutoTradeEventDto event = AutoTradeEventDto.of(
            strategy.getId(),
            account.getId(),
            position,
            eventId,
            System.currentTimeMillis()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent = OutboxEvent.of(OutboxEventType.AUTO_TRADE, eventId, payload);

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException e) {
            throw new TradinException(ExceptionType.SERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }

    public void markAsPublishing(OutboxEvent outboxEvent) {
        outboxEvent.markAsPublishing();
    }

    public void markAsPublished(OutboxEvent outboxEvent) {
        outboxEvent.markAsPublished();
    }

    public void markAsPublishingFailed(OutboxEvent outboxEvent, String errorMessage) {
        outboxEvent.markAsPublishingFailed(errorMessage);
    }

    public void markAsProcessingFailed(OutboxEvent outboxEvent, String errorMessage) {
        outboxEvent.markAsProcessingFailed(errorMessage);
    }

    public void markAsCompleted(OutboxEvent outboxEvent) {
        outboxEvent.markAsCompleted();
    }

    public void markAllAsPublished(List<OutboxEvent> outboxEvents) {
        outboxEvents.forEach(OutboxEvent::markAsPublished);
        outboxEventRepository.saveAll(outboxEvents);
    }

    public void markAllAsPublishingFailed(Map<OutboxEvent, String> failedEvents) {
        failedEvents.forEach(OutboxEvent::markAsProcessingFailed);
        outboxEventRepository.saveAll(failedEvents.keySet());
    }

    private OutboxEvent createOutboxEvent(Strategy strategy, Account account, PositionDto position) {
        String eventId = UUID.randomUUID().toString();
        AutoTradeEventDto event = AutoTradeEventDto.of(
            strategy.getId(),
            account.getId(),
            position,
            eventId,
            System.currentTimeMillis()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            return OutboxEvent.of(OutboxEventType.AUTO_TRADE, eventId, payload);
        } catch (JsonProcessingException e) {
            throw new TradinException(ExceptionType.SERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }
}
