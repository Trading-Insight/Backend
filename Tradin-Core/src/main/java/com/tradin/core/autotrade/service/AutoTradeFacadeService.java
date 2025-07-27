package com.tradin.core.autotrade.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.core.autotrade.service.dto.AutoTradeEventDto;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxStatus;
import com.tradin.core.strategy.service.StrategyService;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.outbox.service.OutBoxMessageService;
import com.tradin.core.futuresOrder.service.FuturesOrderFacadeService;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.autotrade.event.AutoTradeRetryPublisher;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class AutoTradeFacadeService {

    private final StrategyService strategyService;
    private final AccountService accountService;
    private final OutBoxMessageService outBoxMessageService;
    private final FuturesOrderFacadeService futuresOrderFacadeService;
    private final AutoTradeRetryPublisher autoTradeRetryPublisher;
    private final ObjectMapper objectMapper;
    private final Executor executor;

    public AutoTradeFacadeService(
        StrategyService strategyService,
        AccountService accountService,
        OutBoxMessageService outBoxMessageService,
        FuturesOrderFacadeService futuresOrderFacadeService,
        AutoTradeRetryPublisher autoTradeRetryPublisher,
        ObjectMapper objectMapper,
        @Qualifier("autoTradeExecutor") Executor executor
    ) {
        this.strategyService = strategyService;
        this.accountService = accountService;
        this.outBoxMessageService = outBoxMessageService;
        this.futuresOrderFacadeService = futuresOrderFacadeService;
        this.autoTradeRetryPublisher = autoTradeRetryPublisher;
        this.objectMapper = objectMapper;
        this.executor = executor;
    }

    @Transactional
    public void processBatchEvents(List<AutoTradeEventDto> autoTradeEventDtos) {
        // 1. 유효한 이벤트 필터링
        List<OutboxMessage> outboxMessages = outBoxMessageService.findByMessageUuids(collectMessageUuids(autoTradeEventDtos));
        List<OutboxMessage> validOutboxMessages = filterDuplicatedMessages(outboxMessages);

        if (validOutboxMessages.isEmpty()) {
            return;
        }

        // 2. 필요한 데이터 조회
        List<Strategy> strategies = strategyService.findStrategiesByIds(collectStrategyIds(validOutboxMessages));
        List<Account> accounts = accountService.findAllByIds(collectAccountIds(validOutboxMessages));

        // 3. 비동기 자동매매 실행
        List<OutboxMessage> succeedMessages = processAutoTradesAsync(validOutboxMessages, strategies, accounts);

        // 4. 성공한 메시지들 일괄 완료 처리
        if (!succeedMessages.isEmpty()) {
            outBoxMessageService.markAllAsCompleted(collectCompletedIds(succeedMessages));
        }
    }

    /**
     * 여러 계좌에 대해 비동기로 자동매매를 수행
     * - 실패한 계좌는 재시도 토픽에 발행
     * - 성공한 계좌는 리스트에 담아 반환
     */
    private List<OutboxMessage> processAutoTradesAsync(
        List<OutboxMessage> outboxMessages,
        List<Strategy> strategies,
        List<Account> accounts
    ) {
        ConcurrentLinkedQueue<OutboxMessage> succeedMessages = new ConcurrentLinkedQueue<>();

        List<CompletableFuture<Void>> futures = outboxMessages.stream()
            .map(outboxMessage -> CompletableFuture.runAsync(
                () -> processSingleAutoTrade(outboxMessage, strategies, accounts, succeedMessages),
                executor
            ))
            .toList();

        // 모든 비동기 작업 완료 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return new ArrayList<>(succeedMessages);
    }

    /**
     * 단일 자동매매 처리
     * - 성공 시 succeedMessages에 추가
     * - 실패 시 재시도 토픽에 발행
     */
    private void processSingleAutoTrade(
        OutboxMessage outboxMessage,
        List<Strategy> strategies,
        List<Account> accounts,
        ConcurrentLinkedQueue<OutboxMessage> succeedMessages
    ) {
        try {
            // 1. Payload에서 이벤트 데이터 추출
            AutoTradeEventDto eventDto = parseEventFromPayload(outboxMessage);
            
            // 2. 매칭되는 Strategy와 Account 찾기
            Strategy strategy = findMatchedStrategy(strategies, eventDto);
            Account account = findMatchedAccount(accounts, eventDto);
            Position position = eventDto.getPosition().toPosition();

            // 3. 자동매매 실행
            futuresOrderFacadeService.autoTrade(strategy, account, position);

            // 4. 성공 시 리스트에 추가
            succeedMessages.offer(outboxMessage);

        } catch (Exception e) {
            log.error("자동매매 실패: messageId={}, error={}", outboxMessage.getMessageId(), e.getMessage(), e);
            
            // 5. 실패 시 재시도 토픽에 발행
            autoTradeRetryPublisher.publishToRetryTopic(outboxMessage);
        }
    }

    private AutoTradeEventDto parseEventFromPayload(OutboxMessage outboxMessage) {
        try {
            return objectMapper.readValue(outboxMessage.getPayload(), AutoTradeEventDto.class);
        } catch (Exception e) {
            log.error("Payload 파싱 실패: messageId={}, error={}", outboxMessage.getMessageId(), e.getMessage());
            throw new TradinException(ExceptionType.DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
        }
    }

    private static List<String> collectMessageUuids(List<AutoTradeEventDto> autoTradeEventDtos) {
        return autoTradeEventDtos.stream()
            .map(AutoTradeEventDto::getEventId)
            .collect(Collectors.toList());
    }

    public List<OutboxMessage> filterDuplicatedMessages(List<OutboxMessage> allOutboxMessages) {
        return allOutboxMessages.stream()
            .filter(event -> {
                boolean isCompleted = event.getStatus() == OutboxStatus.COMPLETED;

                if (isCompleted) {
                    log.warn("이미 완료된 메시지 수신: messageId={}, status={}",
                        event.getMessageId(), event.getStatus());
                }

                return !isCompleted;
            })
            .collect(Collectors.toList());
    }

    private List<Long> collectStrategyIds(List<OutboxMessage> outboxMessages) {
        return outboxMessages.stream()
            .map(outboxMessage -> {
                try {
                    AutoTradeEventDto eventDto = objectMapper.readValue(outboxMessage.getPayload(), AutoTradeEventDto.class);
                    return eventDto.getStrategyId();
                } catch (Exception e) {
                    log.error("Payload 파싱 실패: messageId={}, error={}", outboxMessage.getMessageId(), e.getMessage());
                    throw new TradinException(ExceptionType.DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
                }
            })
            .collect(Collectors.toList());
    }

    private List<Long> collectAccountIds(List<OutboxMessage> outboxMessages) {
        return outboxMessages.stream()
            .map(outboxMessage -> {
                try {
                    AutoTradeEventDto eventDto = objectMapper.readValue(outboxMessage.getPayload(), AutoTradeEventDto.class);
                    return eventDto.getAccountId();
                } catch (Exception e) {
                    log.error("Payload 파싱 실패: messageId={}, error={}", outboxMessage.getMessageId(), e.getMessage());
                    throw new TradinException(ExceptionType.DESERIALIZATION_FAIL_EXCEPTION, e.getMessage());
                }
            })
            .collect(Collectors.toList());
    }

    private Strategy findMatchedStrategy(List<Strategy> strategies, AutoTradeEventDto eventDto) {
        return strategies.stream()
            .filter(strategy -> strategy.getId().equals(eventDto.getStrategyId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("매칭되는 Strategy를 찾을 수 없습니다"));
    }

    private Account findMatchedAccount(List<Account> accounts, AutoTradeEventDto eventDto) {
        return accounts.stream()
            .filter(account -> account.getId().equals(eventDto.getAccountId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("매칭되는 Account를 찾을 수 없습니다"));
    }

    private static List<Long> collectCompletedIds(List<OutboxMessage> succeedMessages) {
        return succeedMessages.stream()
            .map(OutboxMessage::getId)
            .collect(Collectors.toList());
    }
} 