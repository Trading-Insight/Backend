package com.tradin.module.outbox.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.dto.AutoTradeBatchEventDto;
import com.tradin.module.futures.order.event.dto.AutoTradeEventDto;
import com.tradin.module.futures.order.event.dto.PositionDto;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxEventType;
import com.tradin.module.outbox.domain.repository.OutboxEventRepository;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.users.account.domain.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    final int ACCOUNT_BATCH_SIZE = 50;

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAutoTradingEvents(Strategy strategy, List<Account> accounts, PositionDto position) {

        List<OutboxEvent> events = createBatchOutBoxEvents(strategy, accounts, position);
        batchInsert(events);
    }

    private void batchInsert(List<OutboxEvent> events) {
        String sql = "INSERT INTO outbox_events (event_type, event_id, payload, status, error_message, created_at, updated_at) VALUES (?, ?, ?::jsonb, ?, ?, now(), now())";
        jdbcTemplate.batchUpdate(
            sql,
            events,
            100,
            (ps, event) -> {
                ps.setString(1, event.getEventType().name());
                ps.setString(2, event.getEventId());
                ps.setString(3, event.getPayload());
                ps.setString(4, event.getStatus().name());
                ps.setString(5, event.getErrorMessage());
            }
        );
    }

    /**
     * 계좌를 ACCOUNT_BATCH_SIZE 만큼 묶어서 이벤트를 생성합니다.
     */
    private List<OutboxEvent> createBatchOutBoxEvents(Strategy strategy, List<Account> accounts, PositionDto position) {
        List<OutboxEvent> events = new ArrayList<>();

        for (int i = 0; i < accounts.size(); i += ACCOUNT_BATCH_SIZE) {
            int endIndex = Math.min(i + ACCOUNT_BATCH_SIZE, accounts.size());
            List<Account> accountBatch = accounts.subList(i, endIndex);

            List<Long> accountIds = accountBatch.stream()
                .map(Account::getId)
                .collect(Collectors.toList());

            OutboxEvent batchEvent = createBatchOutboxEvent(strategy, accountIds, position);
            events.add(batchEvent);
        }
        return events;
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

        String sql = "UPDATE outbox_events SET status = ?, updated_at = now() WHERE event_id = ?";
        jdbcTemplate.batchUpdate(
            sql,
            outboxEvents,
            100,
            (ps, event) -> {
                ps.setString(1, event.getStatus().name());
                ps.setString(2, event.getEventId());
            }
        );
    }

    public void markAllAsPublishingFailed(Map<OutboxEvent, String> failedEvents) {
        failedEvents.forEach(OutboxEvent::markAsProcessingFailed);

        String sql = "UPDATE outbox_events SET status = ?, error_message = ?, updated_at = now() WHERE event_id = ?";
        jdbcTemplate.batchUpdate(
            sql,
            failedEvents.keySet(),
            100,
            (ps, event) -> {
                ps.setString(1, event.getStatus().name());
                ps.setString(2, event.getErrorMessage());
                ps.setString(3, event.getEventId());
            }
        );
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

    private OutboxEvent createBatchOutboxEvent(Strategy strategy, List<Long> accountIds, PositionDto position) {
        String eventId = UUID.randomUUID().toString();
        AutoTradeBatchEventDto event = AutoTradeBatchEventDto.of(
            strategy.getId(),
            accountIds,
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
