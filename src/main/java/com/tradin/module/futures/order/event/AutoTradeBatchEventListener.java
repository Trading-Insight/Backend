package com.tradin.module.futures.order.event;

import static com.tradin.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.order.event.dto.AutoTradeEventDto;
import com.tradin.module.futures.order.service.FuturesOrderService;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxStatus;
import com.tradin.module.outbox.implement.OutboxEventProcessor;
import com.tradin.module.outbox.implement.OutboxEventReader;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.implement.AccountReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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

    private final KafkaTemplate<String, String> kafkaTemplate;


    @Qualifier("autoTradeExecutor")
    private final Executor autoTradeExecutor;

    @KafkaListener(
        topics = "auto-trade-topic",
        groupId = "auto-trade-group",
        concurrency = "3",
        batch = "true"
    )
    @Transactional
    public void listenBatch(List<ConsumerRecord<String, String>> records) {
        if (records.isEmpty()) {
            return;
        }

        List<AutoTradeEventDto> autoTradeEventDtos = parseEvents(records);
        List<OutboxEvent> outboxEvents = findNonDuplicatedEvents(autoTradeEventDtos);
        autoTradeEventDtos = collectAutoTradeEvents(autoTradeEventDtos, outboxEvents);
        handleBatchEvents(autoTradeEventDtos, outboxEvents);
    }

    private List<AutoTradeEventDto> parseEvents(List<ConsumerRecord<String, String>> records) {
        return records.stream()
            .map(record -> {
                try {
                    return objectMapper.readValue(record.value(), AutoTradeEventDto.class);
                } catch (Exception e) {
                    log.error("이벤트 파싱 실패: record={}, error={}", record.value(), e.getMessage());
                    throw new TradinException(
                        DESERIALIZATION_FAIL_EXCEPTION,
                        String.format("이벤트 파싱 실패: %s", e.getMessage())
                    );
                }
            })
            .toList();
    }

    private void handleBatchEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxEvent> outboxEvents) {
        List<Strategy> strategies = strategyReader.findStrategiesByIds(collectStrategyIds(autoTradeEventDtos));
        List<Account> accounts = accountReader.findAccountsByIds(collectAccountIds(autoTradeEventDtos));

        List<AutoTradeEventDto> successEvents = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture
            .allOf(
                submitAutoTradeTasks(autoTradeEventDtos, strategies, accounts, successEvents, outboxEvents)
                    .toArray(CompletableFuture[]::new)
            )
            .join();

        if (!successEvents.isEmpty()) {
            List<Long> ids = getSuccessedOutboxEventIds(outboxEvents, successEvents);
            outboxEventProcessor.markAllAsCompleted(ids);
        }
    }

    private List<CompletableFuture<Void>> submitAutoTradeTasks(List<AutoTradeEventDto> autoTradeEventDtos, List<Strategy> strategies, List<Account> accounts, List<AutoTradeEventDto> successEvents, List<OutboxEvent> outboxEvents) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (AutoTradeEventDto dto : autoTradeEventDtos) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    boolean success = executeAutoTrade(dto, strategies, accounts, outboxEvents);
                    if (success) {
                        synchronized (successEvents) {
                            successEvents.add(dto);
                        }
                    }
                }, autoTradeExecutor
            );
            futures.add(future);
        }
        return futures;
    }

    private boolean executeAutoTrade(AutoTradeEventDto dto, List<Strategy> strategies, List<Account> accounts, List<OutboxEvent> outboxEvents) {
        try {
            Strategy strategy = getMatchedStrategy(strategies, dto);
            Account account = getMatchedAccount(accounts, dto);
            Position position = getMatchedPosition(List.of(dto), dto);

            futuresOrderService.autoTrade(strategy, account, position);

            return true;
        } catch (Exception e) {
            log.error("이벤트 처리 실패: eventId={}, error={}", dto.getEventId(), e.getMessage(), e);
            OutboxEvent outBoxEvent = getMatchedOutBoxEvent(dto, outboxEvents);
            publishToRetryTopic(outBoxEvent);
            return false;
        }
    }

    private static List<AutoTradeEventDto> collectAutoTradeEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxEvent> outboxEvents) {
        return autoTradeEventDtos.stream()
            .filter(event -> outboxEvents.stream()
                .anyMatch(outboxEvent -> outboxEvent.getEventId().equals(event.getEventId())))
            .collect(Collectors.toList());
    }

    private static List<Long> collectAccountIds(List<AutoTradeEventDto> events) {
        return events.stream().map(AutoTradeEventDto::getAccountId).collect(Collectors.toList());
    }

    private List<String> collectEventUuids(List<AutoTradeEventDto> events) {
        return events.stream()
            .map(AutoTradeEventDto::getEventId)
            .collect(Collectors.toList());
    }

    private static List<Long> getSuccessedOutboxEventIds(List<OutboxEvent> outboxEvents, List<AutoTradeEventDto> successEvents) {
        return outboxEvents.stream()
            .filter(event -> successEvents.stream()
                .anyMatch(successEvent -> successEvent.getEventId().equals(event.getEventId())))
            .map(OutboxEvent::getId)
            .collect(Collectors.toList());
    }

    private List<Long> collectStrategyIds(List<AutoTradeEventDto> autoTradeEventDtos) {
        return autoTradeEventDtos.stream()
            .map(AutoTradeEventDto::getStrategyId)
            .collect(Collectors.toList());
    }


    private static Position getMatchedPosition(List<AutoTradeEventDto> autoTradeEventDtos, AutoTradeEventDto autoTradeEventDto) {
        return autoTradeEventDtos.stream()
            .filter(event -> event.getEventId().equals(autoTradeEventDto.getEventId()))
            .findFirst()
            .map(AutoTradeEventDto::getPosition)
            .orElseThrow(() -> new TradinException(DESERIALIZATION_FAIL_EXCEPTION)).toPosition();
    }

    private static Account getMatchedAccount(List<Account> accounts, AutoTradeEventDto autoTradeEventDto) {
        return accounts.stream()
            .filter(a -> a.getId().equals(autoTradeEventDto.getAccountId()))
            .findFirst()
            .orElseThrow(() -> new TradinException(
                DESERIALIZATION_FAIL_EXCEPTION));
    }

    private Strategy getMatchedStrategy(List<Strategy> strategies, AutoTradeEventDto autoTradeEventDto) {
        return strategies.stream()
            .filter(s -> s.getId().equals(autoTradeEventDto.getStrategyId()))
            .findFirst()
            .orElseThrow(() -> new TradinException(
                DESERIALIZATION_FAIL_EXCEPTION));
    }

    private static OutboxEvent getMatchedOutBoxEvent(AutoTradeEventDto dto, List<OutboxEvent> outboxEvents) {
        return outboxEvents.stream()
            .filter(event -> event.getEventId().equals(dto.getEventId()))
            .findFirst()
            .orElseThrow(() -> new TradinException(DESERIALIZATION_FAIL_EXCEPTION));
    }

    private void publishToRetryTopic(OutboxEvent outBoxEvent) {
        String retryTopic = "auto-trade-retry-topic";
        try {
            String message = objectMapper.writeValueAsString(outBoxEvent);
            kafkaTemplate.send(retryTopic, message);
        } catch (Exception e) {
            log.error("재처리 토픽 전송 실패: eventId={}, topic={}, error={}", outBoxEvent.getEventId(), retryTopic, e.getMessage(), e);
        }
    }

    private List<OutboxEvent> findNonDuplicatedEvents(List<AutoTradeEventDto> autoTradeEventDtos) {
        List<OutboxEvent> outboxEvents = outboxEventReader.findByEventUuids(collectEventUuids(autoTradeEventDtos));

        return outboxEvents.stream()
            .filter(event ->
                event.getStatus() == OutboxStatus.PUBLISHED ||
                    event.getStatus() == OutboxStatus.PUBLISHING_FAILED
            )
            .collect(Collectors.toList());
    }
}