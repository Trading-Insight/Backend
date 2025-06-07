package com.tradin.module.outbox.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.AutoTradeEventDto;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxEventType;
import com.tradin.module.outbox.domain.repository.OutboxEventRepository;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;


    @Async("autoTradeExecutor") //TODO - 스레드풀 관리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAutoTradingEvent(Strategy strategy, Account account, Position position) {
        String eventId = UUID.randomUUID().toString();
        AutoTradeEventDto event = new AutoTradeEventDto(
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

    public void markAsPublished(OutboxEvent outboxEvent) {
        outboxEvent.markAsPublished();
    }

    public void markAsFailed(OutboxEvent outboxEvent, String errorMessage) {
        outboxEvent.markAsFailed(errorMessage);
    }


    public void markAsCompleted(OutboxEvent outboxEvent) {
        outboxEvent.markAsCompleted();
    }
}
