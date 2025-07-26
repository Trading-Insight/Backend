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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tradin.core.futuresOrder.event.dto.AutoTradeEventDto;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.futuresOrder.service.FuturesOrderFacadeService;

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
    public void createStrategy() {
        strategyService.createStrategy();
    }

    @Transactional
    public void handleFutureWebHook(WebHookDto request) {
        // 1. 전략 검증 및 업데이트
        strategyService.handleFutureWebHook(request);
        
        // 2. 기존 거래내역에 종료 거래 업데이트
        Strategy strategy = strategyService.findStrategyById(request.getId());
        Position position = request.getPosition();
        historyService.closeOpenHistory(strategy.getId(), position);
        
        // 3. 거래내역 생성
        historyService.createHistory(strategy, position);
        
        // 4. 구독 계좌 자동매매
        autoTrading(strategy, position);
    }

    private void autoTrading(Strategy strategy, Position position) {
        List<Account> accounts = subscriptionService.findSubscribedAccountsByStrategyId(strategy.getId());
        outBoxMessageService.publishAutoTradingEvents(strategy, accounts, position.toDto());
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
