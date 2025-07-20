package com.tradin.core.outbox.implement;


import static com.tradin.core.outbox.domain.QOutboxMessage.outboxMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.account.domain.Account;
import com.tradin.core.futuresOrder.event.dto.AutoTradeEventDto;
import com.tradin.core.futuresOrder.event.dto.PositionDto;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxMessageType;
import com.tradin.core.outbox.domain.repository.OutboxMessageRepository;
import com.tradin.core.strategy.domain.Strategy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxMessageProcessor {

    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAutoTradingMessages(Strategy strategy, List<Account> accounts, PositionDto position) {
        List<OutboxMessage> messages = new ArrayList<>();

        //TODO
        for (Account account : accounts) {
            OutboxMessage message = createOutboxMessage(strategy, account, position);
            messages.add(message);
        }

        batchInsert(messages);
    }

    private void batchInsert(List<OutboxMessage> messages) {
        String sql = "INSERT INTO outbox_message (message_type, message_id, payload, status, error_message, created_at, updated_at) VALUES (?, ?, ?::jsonb, ?, ?, now(), now())";

        jdbcTemplate.batchUpdate(
            sql,
            messages,
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

    public void publishAutoTradingMessage(Strategy strategy, Account account, PositionDto position) {
        String messageId = UUID.randomUUID().toString();

        AutoTradeEventDto event = AutoTradeEventDto.of(
            strategy.getId(),
            account.getId(),
            position,
            messageId,
            System.currentTimeMillis()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxMessage outboxMessage = OutboxMessage.of(OutboxMessageType.AUTO_TRADE, messageId, payload);

            outboxMessageRepository.save(outboxMessage);
        } catch (JsonProcessingException e) {
            throw new TradinException(ExceptionType.SERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }

    public void markAsPublishing(OutboxMessage outboxMessage) {
        outboxMessage.markAsPublishing();
    }

    public void markAsPublished(OutboxMessage outboxMessage) {
        outboxMessage.markAsPublished();
    }

    public void markAsPublishingFailed(OutboxMessage outboxMessage, String errorMessage) {
        outboxMessage.markAsPublishingFailed(errorMessage);
    }

    public void markAsProcessingFailed(OutboxMessage outboxMessage, String errorMessage) {
        outboxMessage.markAsProcessingFailed(errorMessage);
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
        String messageId = UUID.randomUUID().toString();
        AutoTradeEventDto event = AutoTradeEventDto.of(
            strategy.getId(),
            account.getId(),
            position,
            messageId,
            System.currentTimeMillis()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            return OutboxMessage.of(OutboxMessageType.AUTO_TRADE, messageId, payload);
        } catch (JsonProcessingException e) {
            throw new TradinException(ExceptionType.SERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }
}
