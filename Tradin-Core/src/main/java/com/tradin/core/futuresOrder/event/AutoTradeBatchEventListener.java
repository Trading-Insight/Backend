package com.tradin.core.futuresOrder.event;

import static com.tradin.core.common.exception.ExceptionType.DESERIALIZATION_FAIL_EXCEPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.implement.AccountReader;
import com.tradin.core.futuresOrder.event.dto.AutoTradeEventDto;
import com.tradin.core.futuresOrder.service.FuturesOrderService;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxStatus;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
import com.tradin.core.outbox.implement.OutboxMessageReader;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.implement.StrategyReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class AutoTradeBatchEventListener {

    private final FuturesOrderService futuresOrderService;
    private final StrategyReader strategyReader;
    private final AccountReader accountReader;
    private final OutboxMessageReader outboxMessageReader;
    private final OutboxMessageProcessor outboxMessageProcessor;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Executor autoTradeExecutor;

    public AutoTradeBatchEventListener(
            FuturesOrderService futuresOrderService,
            StrategyReader strategyReader,
            AccountReader accountReader,
            OutboxMessageReader outboxMessageReader,
            OutboxMessageProcessor outboxMessageProcessor,
            ObjectMapper objectMapper,
            KafkaTemplate<String, String> kafkaTemplate,
            @Qualifier("autoTradeExecutor") Executor autoTradeExecutor) {
        this.futuresOrderService = futuresOrderService;
        this.strategyReader = strategyReader;
        this.accountReader = accountReader;
        this.outboxMessageReader = outboxMessageReader;
        this.outboxMessageProcessor = outboxMessageProcessor;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.autoTradeExecutor = autoTradeExecutor;
    }

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
        try {
            List<AutoTradeEventDto> autoTradeEventDtos = parseEvents(records);
            List<OutboxMessage> outboxMessages = findNonDuplicatedMessages(autoTradeEventDtos);
            autoTradeEventDtos = collectAutoTradeEvents(autoTradeEventDtos, outboxMessages);
            handleBatchEvents(autoTradeEventDtos, outboxMessages);
        } catch (Exception e) {
            log.error("자동매매 이벤트 처리 실패: error={}", e.getMessage(), e);
        }
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

    private void handleBatchEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxMessage> outboxMessages) {
        List<Strategy> strategies = strategyReader.findStrategiesByIds(collectStrategyIds(autoTradeEventDtos));
        List<Account> accounts = accountReader.findAccountsByIds(collectAccountIds(autoTradeEventDtos));

        List<AutoTradeEventDto> successEvents = Collections.synchronizedList(new ArrayList<>());

        CompletableFuture
            .allOf(
                submitAutoTradeTasks(autoTradeEventDtos, strategies, accounts, successEvents, outboxMessages)
                    .toArray(CompletableFuture[]::new)
            )
            .join();

        if (!successEvents.isEmpty()) {
            List<Long> ids = getSuccessedOutboxMessageIds(outboxMessages, successEvents);
            outboxMessageProcessor.markAllAsCompleted(ids);
        }
    }

    private List<CompletableFuture<Void>> submitAutoTradeTasks(List<AutoTradeEventDto> autoTradeEventDtos, List<Strategy> strategies, List<Account> accounts, List<AutoTradeEventDto> successEvents, List<OutboxMessage> outboxMessages) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (AutoTradeEventDto dto : autoTradeEventDtos) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    boolean success = executeAutoTrade(dto, strategies, accounts, outboxMessages);
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

    private boolean executeAutoTrade(AutoTradeEventDto dto, List<Strategy> strategies, List<Account> accounts, List<OutboxMessage> outboxMessages) {
        try {
            Strategy strategy = getMatchedStrategy(strategies, dto);
            Account account = getMatchedAccount(accounts, dto);
            Position position = getMatchedPosition(List.of(dto), dto);

            futuresOrderService.autoTrade(strategy, account, position);

            return true;
        } catch (Exception e) {
            log.error("이벤트 처리 실패: eventId={}, error={}", dto.getEventId(), e.getMessage(), e);

            publishToRetryTopic(dto);
            return false;
        }
    }

    private static List<AutoTradeEventDto> collectAutoTradeEvents(List<AutoTradeEventDto> autoTradeEventDtos, List<OutboxMessage> outboxMessages) {
        return autoTradeEventDtos.stream()
            .filter(event -> outboxMessages.stream()
                .anyMatch(outboxMessage -> outboxMessage.getMessageId().equals(event.getEventId())))
            .collect(Collectors.toList());
    }

    private static List<Long> collectAccountIds(List<AutoTradeEventDto> events) {
        return events.stream().map(AutoTradeEventDto::getAccountId).collect(Collectors.toList());
    }

    private List<String> collectMessageIds(List<AutoTradeEventDto> events) {
        return events.stream()
            .map(AutoTradeEventDto::getEventId)
            .collect(Collectors.toList());
    }

    private static List<Long> getSuccessedOutboxMessageIds(List<OutboxMessage> outboxMessages, List<AutoTradeEventDto> successEvents) {
        return outboxMessages.stream()
            .filter(message -> successEvents.stream()
                .anyMatch(successEvent -> successEvent.getEventId().equals(message.getMessageId())))
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

    private static OutboxMessage getMatchedOutBoxMessage(AutoTradeEventDto dto, List<OutboxMessage> outboxMessages) {
        return outboxMessages.stream()
            .filter(message -> message.getMessageId().equals(dto.getEventId()))
            .findFirst()
            .orElseThrow(() -> new TradinException(DESERIALIZATION_FAIL_EXCEPTION));
    }

    private void publishToRetryTopic(AutoTradeEventDto autoTradeEventDto) {
        String retryTopic = "auto-trade-retry-topic";
        try {
            String message = objectMapper.writeValueAsString(autoTradeEventDto);
            kafkaTemplate.send(retryTopic, message);
        } catch (Exception e) {
            log.error("재처리 토픽 전송 실패: messageId={}, topic={}, error={}", autoTradeEventDto.getEventId(), retryTopic, e.getMessage(), e);
        }
    }

    private List<OutboxMessage> findNonDuplicatedMessages(List<AutoTradeEventDto> autoTradeEventDtos) {
        List<OutboxMessage> outboxMessages = outboxMessageReader.findByMessageIds(collectMessageIds(autoTradeEventDtos));

        return outboxMessages.stream()
            .filter(message -> message.getStatus() == OutboxStatus.PUBLISHED)
            .collect(Collectors.toList());
    }
}