package com.tradin.module.futures.order.event;

import static com.tradin.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.dto.AutoTradeBatchEventDto;
import com.tradin.module.futures.order.event.dto.PositionDto;
import com.tradin.module.futures.order.service.FuturesOrderService;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.implement.OutboxEventProcessor;
import com.tradin.module.outbox.implement.OutboxEventReader;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.implement.AccountReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
public class AutoTradeBatchEventListener {

    private final FuturesOrderService futuresOrderService;
    private final StrategyReader strategyReader;
    private final AccountReader accountReader;
    private final OutboxEventReader outboxEventReader;
    private final OutboxEventProcessor outboxEventProcessor;
    private final ObjectMapper objectMapper;

    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
        topics = "auto-trade-topic",
        groupId = "auto-trade-batch-group",
        concurrency = "5"
    )
    @Transactional
    public void listenBatch(String message) {
        AutoTradeBatchEventDto event;
        try {
            event = objectMapper.readValue(message, AutoTradeBatchEventDto.class);
        } catch (Exception e) {
            throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }

        OutboxEvent outboxEvent = outboxEventReader.findByEventId(event.getEventId());
        Strategy strategy = strategyReader.findStrategyById(event.getStrategyId());
        PositionDto positionDto = event.getPosition();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Long accountId : event.getAccountIds()) {
            Account account = accountReader.findAccountById(accountId);

            CompletableFuture<Void> future = futuresOrderService.autoTradeAsync(strategy, account, positionDto.toPosition());
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        outboxEventProcessor.markAsCompleted(outboxEvent);
    }

    @DltHandler
    public void handleDlq(String message, Exception exception) {
        AutoTradeBatchEventDto event;
        try {
            event = objectMapper.readValue(message, AutoTradeBatchEventDto.class);
        } catch (Exception e) {
            throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }

        OutboxEvent outboxEvent = outboxEventReader.findByEventId(event.getEventId());
        outboxEventProcessor.markAsProcessingFailed(outboxEvent, exception.getMessage());
    }
}