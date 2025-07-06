package com.tradin.module.futures.order.event;

import static com.tradin.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.dto.AutoTradeEventDto;
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
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(
        topics = "auto-trade-topic",
        groupId = "auto-trade-group",
        concurrency = "5",
        batch = "true"
    )
    @Transactional
    public void listenBatch(List<ConsumerRecord<String, String>> records) {
        List<AutoTradeEventDto> events = new ArrayList<>();
        List<OutboxEvent> outboxEvents = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            try {
                AutoTradeEventDto event = objectMapper.readValue(record.value(), AutoTradeEventDto.class);
                events.add(event);

                OutboxEvent outboxEvent = outboxEventReader.findByEventId(event.getEventId());
                outboxEvents.add(outboxEvent);

            } catch (Exception e) {
                log.error("이벤트 파싱 실패: record={}, error={}", record.value(), e.getMessage());
                throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
            }
        }

        // 배치 처리
        processBatchAsync(events, outboxEvents);
    }

    private void processBatchAsync(List<AutoTradeEventDto> events, List<OutboxEvent> outboxEvents) {
        Strategy strategy = strategyReader.findStrategyById(events.get(0).getStrategyId());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            AutoTradeEventDto event = events.get(i);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Account account = accountReader.findAccountById(event.getAccountId());
                futuresOrderService.autoTrade(strategy, account, event.getPosition().toPosition());
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @DltHandler
    public void handleDlq(String message, Exception exception) {
        AutoTradeEventDto event;
        try {
            event = objectMapper.readValue(message, AutoTradeEventDto.class);
        } catch (Exception e) {
            throw new TradinException(DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }

        OutboxEvent outboxEvent = outboxEventReader.findByEventId(event.getEventId());
        outboxEventProcessor.markAsProcessingFailed(outboxEvent, exception.getMessage());

        log.error(
            "DLQ 처리: accountId={}, eventId={}, error={}",
            event.getAccountId(), event.getEventId(), exception.getMessage()
        );
    }
}