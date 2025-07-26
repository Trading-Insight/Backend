package com.tradin.core.futuresOrder.event;

import static com.tradin.core.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.account.domain.Account;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.futuresOrder.event.dto.AutoTradeEventDto;
import com.tradin.core.futuresOrder.service.FuturesOrderFacadeService;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxStatus;
import com.tradin.core.outbox.service.OutBoxMessageFacadeService;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.service.StrategyFacadeService;
import com.tradin.core.account.service.AccountFacadeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTradeBatchEventListener {

    private final FuturesOrderFacadeService futuresOrderFacadeService;
    private final StrategyFacadeService strategyFacadeService;
    private final AccountFacadeService accountFacadeService;
    private final OutBoxMessageFacadeService outBoxMessageFacadeService;
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
        List<OutboxMessage> OutboxMessages = findNonDuplicatedEvents(autoTradeEventDtos);
        autoTradeEventDtos = collectAutoTradeEvents(autoTradeEventDtos, OutboxMessages);
        handleBatchEvents(autoTradeEventDtos, OutboxMessages);
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

    private void handleBatchEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxMessage> OutboxMessages) {
        List<Strategy> strategies = strategyFacadeService.findStrategiesByIds(collectStrategyIds(autoTradeEventDtos));
        List<Account> accounts = accountFacadeService.findAccountsByIds(collectAccountIds(autoTradeEventDtos));

        List<AutoTradeEventDto> successEvents = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture
            .allOf(
                submitAutoTradeTasks(autoTradeEventDtos, strategies, accounts, successEvents, OutboxMessages)
                    .toArray(CompletableFuture[]::new)
            )
            .join();

        if (!successEvents.isEmpty()) {
            List<Long> ids = getSuccessedOutboxMessageIds(OutboxMessages, successEvents);
            outBoxMessageFacadeService.markAllAsCompleted(ids);
        }
    }

    private List<CompletableFuture<Void>> submitAutoTradeTasks(List<AutoTradeEventDto> autoTradeEventDtos, List<Strategy> strategies, List<Account> accounts, List<AutoTradeEventDto> successEvents, List<OutboxMessage> OutboxMessages) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (AutoTradeEventDto dto : autoTradeEventDtos) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    boolean success = executeAutoTrade(dto, strategies, accounts, OutboxMessages);
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

    private boolean executeAutoTrade(AutoTradeEventDto dto, List<Strategy> strategies, List<Account> accounts, List<OutboxMessage> OutboxMessages) {
        try {
            Strategy strategy = getMatchedStrategy(strategies, dto);
            Account account = getMatchedAccount(accounts, dto);
            Position position = getMatchedPosition(List.of(dto), dto);

            futuresOrderFacadeService.autoTrade(strategy, account, position);

            return true;
        } catch (Exception e) {
            log.error("이벤트 처리 실패: eventId={}, error={}", dto.getEventId(), e.getMessage(), e);
            OutboxMessage OutboxMessage = getMatchedOutboxMessage(dto, OutboxMessages);
            publishToRetryTopic(OutboxMessage);
            return false;
        }
    }

    private static List<AutoTradeEventDto> collectAutoTradeEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxMessage> OutboxMessages) {
        return autoTradeEventDtos.stream()
            .filter(event -> OutboxMessages.stream()
                .anyMatch(OutboxMessage -> OutboxMessage.getMessageId().equals(event.getEventId())))
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

    private static List<Long> getSuccessedOutboxMessageIds(List<OutboxMessage> OutboxMessages, List<AutoTradeEventDto> successEvents) {
        return OutboxMessages.stream()
            .filter(event -> successEvents.stream()
                .anyMatch(successEvent -> successEvent.getEventId().equals(event.getMessageId())))
            .map(OutboxMessage::getId)
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

    private static OutboxMessage getMatchedOutboxMessage(AutoTradeEventDto dto, List<OutboxMessage> OutboxMessages) {
        return OutboxMessages.stream()
            .filter(event -> event.getMessageId().equals(dto.getEventId()))
            .findFirst()
            .orElseThrow(() -> new TradinException(DESERIALIZATION_FAIL_EXCEPTION));
    }

    private void publishToRetryTopic(OutboxMessage OutboxMessage) {
        String retryTopic = "auto-trade-retry-topic";
        try {
            String message = objectMapper.writeValueAsString(OutboxMessage);
            kafkaTemplate.send(retryTopic, message);
        } catch (Exception e) {
            log.error("재처리 토픽 전송 실패: eventId={}, topic={}, error={}", OutboxMessage.getMessageId(), retryTopic, e.getMessage(), e);
        }
    }

    private List<OutboxMessage> findNonDuplicatedEvents(List<AutoTradeEventDto> autoTradeEventDtos) {
        List<OutboxMessage> outboxMessages = outBoxMessageFacadeService.findByEventUuids(collectEventUuids(autoTradeEventDtos));

        return outboxMessages.stream()
            .filter(event -> event.getStatus() != OutboxStatus.COMPLETED)
            .collect(Collectors.toList());
    }
}