package com.tradin.core.strategy.service;

import com.tradin.core.strategy.service.dto.FindStrategiesInfoResponseDto;
import com.tradin.core.strategy.service.dto.WebHookDto;
import com.tradin.core.history.service.HistoryService;
import com.tradin.core.subscription.service.SubscriptionService;
import com.tradin.core.outbox.service.OutBoxMessageService;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.account.domain.Account;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StrategyFacadeService {
    private final StrategyService strategyService;
    private final HistoryService historyService;
    private final SubscriptionService subscriptionService;
    private final OutBoxMessageService outBoxMessageService;

    @Transactional(readOnly = true)
    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        return strategyService.findFutureStrategiesInfo();
    }

    @Transactional(readOnly = true)
    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        return strategyService.findSpotStrategiesInfo();
    }

    @Transactional
    public void createTestStrategy() {
        strategyService.createStrategy();
    }

    @Transactional
    public void updateStrategyAndHistoryMetaData(WebHookDto request) {
        // 1. 전략 검증 및 업데이트
        Strategy strategy = strategyService.updateStrategyStatistics(request);
        
        // 2. 종료 거래 업데이트 & 신규 거래내역 생성
        Position position = request.getPosition();
        historyService.closeAndCreateHistory(strategy, position);
    }

    @Async("autoTradeExecutor")
    public void publishAutoTradingMessages(WebHookDto request) {
        Strategy strategy = strategyService.findStrategyById(request.getId());
        Position position = request.getPosition();

        List<Account> accounts = subscriptionService.findSubscribedAccountsByStrategyId(strategy.getId());
        outBoxMessageService.publishAutoTradingMessages(strategy, accounts, position.toDto());
    }

    @Transactional(readOnly = true)
    public List<Strategy> findStrategiesByIds(List<Long> strategyIds) {
        return strategyService.findStrategiesByIds(strategyIds);
    }

    @Transactional(readOnly = true)
    public Strategy findStrategyById(Long strategyId) {
        return strategyService.findStrategyById(strategyId);
    }
}
