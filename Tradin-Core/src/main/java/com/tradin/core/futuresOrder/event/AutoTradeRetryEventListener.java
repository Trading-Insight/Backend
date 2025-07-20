package com.tradin.core.futuresOrder.event;

import static com.tradin.core.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.implement.AccountReader;
import com.tradin.core.futuresOrder.event.dto.AutoTradeEventDto;
import com.tradin.core.futuresOrder.event.dto.PositionDto;
import com.tradin.core.futuresOrder.implement.FuturesOrderProcessor;
import com.tradin.core.futuresOrder.service.FuturesOrderService;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
import com.tradin.core.outbox.implement.OutboxMessageReader;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.implement.StrategyReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTradeRetryEventListener {

    private final FuturesOrderProcessor futuresOrderProcessor;
    private final StrategyReader strategyReader;
    private final AccountReader accountReader;
    private final OutboxMessageReader outboxMessageReader;
    private final OutboxMessageProcessor outboxMessageProcessor;
    private final FuturesOrderService futuresOrderService;
    private final ObjectMapper objectMapper;

    @RetryableTopic(
        attempts = "2",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
        topics = "auto-trade-retry-topic",
        groupId = "auto-trade-retry-group",
        concurrency = "1"
    )
    @Transactional
    public void listen(String message) {
        AutoTradeEventDto event;
        try {
            event = objectMapper.readValue(message, AutoTradeEventDto.class);
        } catch (Exception e) {
            throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }

        OutboxMessage outboxMessage = outboxMessageReader.findByMessageId(event.getEventId());
        Strategy strategy = strategyReader.findStrategyById(event.getStrategyId());
        Account account = accountReader.findAccountById(event.getAccountId());
        PositionDto positionDto = event.getPosition();

        futuresOrderService.autoTrade(strategy, account, positionDto.toPosition());
        outboxMessageProcessor.markAsCompleted(outboxMessage);
    }

    @DltHandler
    public void handleDlq(String message, Exception exception) {
        AutoTradeEventDto event;
        try {
            event = objectMapper.readValue(message, AutoTradeEventDto.class);
        } catch (Exception e) {
            throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }

        OutboxMessage outboxMessage = outboxMessageReader.findByMessageId(event.getEventId());
        outboxMessageProcessor.markAsProcessingFailed(outboxMessage, exception.getMessage());
    }
} 