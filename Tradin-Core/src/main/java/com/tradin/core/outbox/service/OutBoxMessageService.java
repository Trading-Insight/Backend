package com.tradin.core.outbox.service;

import static com.tradin.core.outbox.domain.OutboxMessageType.AUTO_TRADE;
import static com.tradin.core.outbox.domain.OutboxStatus.PENDING;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.account.domain.Account;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.autotrade.service.dto.AutoTradeEventDto;
import com.tradin.core.autotrade.service.dto.PositionDto;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.repository.OutboxMessageRepository;
import com.tradin.core.outbox.service.dto.OutBoxMessagesEvent;
import com.tradin.core.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutBoxMessageService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    public void publishAutoTradingMessages(Strategy strategy, List<Account> accounts, PositionDto position) {
        List<OutboxMessage> messages = new ArrayList<>();

        for (Account account : accounts) {
            OutboxMessage event = createOutboxMessage(strategy, account, position);
            messages.add(event);
        }

        batchInsert(messages);
        publishEvent();
    }

    private void publishEvent() {
        List<OutboxMessage> messages = outboxMessageRepository.findAllByTypeAndStatus(AUTO_TRADE, PENDING);
        applicationEventPublisher.publishEvent(new OutBoxMessagesEvent(messages));
    }

    private void batchInsert(List<OutboxMessage> events) {
        String sql = "INSERT INTO outbox_message (message_type, message_id, payload, status, error_message, created_at, updated_at) VALUES (?, ?, ?::jsonb, ?, ?, now(), now())";

        jdbcTemplate.batchUpdate(
            sql,
            events,
            100,
            (ps, message) -> {
                ps.setString(1, message.getMessageType().name());
                ps.setString(2, message.getMessageId());
                ps.setString(3, message.getPayload());
                ps.setString(4, message.getStatus().name());
                ps.setString(5, message.getErrorMessage());
            }
        );
    }

    public List<OutboxMessage> findAllPendingEvents() {
        return outboxMessageRepository.findAllByStatus(PENDING);
    }

    public OutboxMessage findByEventId(String eventId) {
        return outboxMessageRepository.findByMessageId(eventId)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_OUTBOX_MESSAGE_EXCEPTION));
    }

    public List<OutboxMessage> findByMessageUuids(List<String> eventUuids) {
        return outboxMessageRepository.findAllByMessageIdIn(eventUuids);
    }

    public void markAsCompleted(OutboxMessage outboxMessage) {
        outboxMessage.markAsCompleted();
    }

    public void markAllAsPublished(List<OutboxMessage> outboxMessages) {
        List<Long> ids = outboxMessages.stream()
            .map(OutboxMessage::getId)
            .toList();

        outboxMessageRepository.markAllAsPublished(ids);
    }

    public void markAllAsCompleted(List<Long> ids) {
        outboxMessageRepository.markAllAsCompleted(ids);
    }

    public void markAllAsPublishingFailed(List<OutboxMessage> outboxMessages) {
        String sql = "UPDATE outbox_message SET status = ?, error_message = ?, updated_at = now() WHERE message_id = ?";
        jdbcTemplate.batchUpdate(
            sql,
            outboxMessages,
            100,
            (ps, message) -> {
                ps.setString(1, message.getStatus().name());
                ps.setString(2, message.getErrorMessage());
                ps.setString(3, message.getMessageId());
            }
        );
    }

    private OutboxMessage createOutboxMessage(Strategy strategy, Account account, PositionDto position) {
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
            return OutboxMessage.of(AUTO_TRADE, eventId, payload);
        } catch (JsonProcessingException e) {
            throw new TradinException(ExceptionType.SERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }

    public void markAsProcessingFailed(OutboxMessage outboxMessage, String errorMessage) {
        outboxMessage.markAsProcessingFailed(errorMessage);
    }
}
