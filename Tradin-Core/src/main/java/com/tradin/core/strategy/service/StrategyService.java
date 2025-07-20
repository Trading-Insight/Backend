package com.tradin.core.strategy.service;

import static com.tradin.core.common.exception.ExceptionType.SAME_POSITION_REQUEST_EXCEPTION;

import com.tradin.core.account.domain.Account;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.account.implement.AccountReader;
import com.tradin.core.balance.implement.BalanceProcessor;
import com.tradin.core.balance.implement.BalanceReader;
import com.tradin.core.futuresOrder.event.dto.PositionDto;
import com.tradin.core.futuresOrder.implement.FuturesOrderProcessor;
import com.tradin.core.futuresPosition.implement.FuturesPositionProcessor;
import com.tradin.core.history.domain.History;
import com.tradin.core.history.implement.HistoryProcessor;
import com.tradin.core.history.implement.HistoryReader;
import com.tradin.core.outbox.implement.OutboxMessageProcessor;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.core.strategy.implement.StrategyProcessor;
import com.tradin.core.strategy.implement.StrategyReader;
import com.tradin.core.strategy.service.dto.FindStrategiesInfoResponseDto;
import com.tradin.core.strategy.service.dto.WebHookDto;
import com.tradin.core.subscription.implement.SubscriptionReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

    private final AccountReader accountReader;
    private final BalanceReader balanceReader;
    private final StrategyReader strategyReader;
    private final HistoryReader historyReader;
    private final SubscriptionReader subscriptionReader;
    private final StrategyProcessor strategyProcessor;
    private final HistoryProcessor historyProcessor;
    private final FuturesOrderProcessor futuresOrderProcessor;
    private final FuturesPositionProcessor futuresPositionProcessor;
    private final BalanceProcessor balanceProcessor;
    private final OutboxMessageProcessor outboxMessageProcessor;

    @Transactional
    public void createStrategy() {
        strategyProcessor.createStrategy();
    }


    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyReader.findFutureStrategyInfoDaos();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyReader.findSpotStrategyInfoDaos();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

    /**
     * 1. 전략 검증  2. 기존 거래내역에 종료 거래 업데이트 3. 전략 업데이트 (수익률, 매매 횟수 등) 4. 거래내역 생성 5. 구독 계좌 자동매매
     */
    @Transactional
    public void handleFutureWebHook(WebHookDto request) {
        Strategy strategy = validateExistStrategy(request.getId());
        Position position = request.getPosition();

        validateSamePosition(strategy, position);
        closeOpenHistory(strategy, position);
        updateStrategy(strategy, position);
        createHistory(strategy, position);
        autoTrading(strategy, position);
    }

    private Strategy validateExistStrategy(Long id) {
        return strategyReader.findStrategyById(id);
    }

    private void validateSamePosition(Strategy strategy, Position position) {
        if (strategy.getCurrentPosition().getTradingType() == position.getTradingType()) {
            throw new TradinException(SAME_POSITION_REQUEST_EXCEPTION);
        }
    }


    private void updateStrategy(Strategy strategy, Position strategyPosition) {
        strategyProcessor.updateRateAndCount(strategy, strategyPosition);
        strategyProcessor.updateCurrentPosition(strategy, strategyPosition); //TODO
    }

    private void closeOpenHistory(Strategy strategy, Position strategyPosition) {
        History history = historyReader.findOpenHistoryByStrategyId(strategy.getId());
        historyProcessor.closeHistory(history, strategyPosition);
    }

    private void createHistory(Strategy strategy, Position stratgeyPosition) {
        historyProcessor.createHistory(strategy, stratgeyPosition);
    }

    public void autoTrading(Strategy strategy, Position strategyPosition) {
        List<Account> accounts = subscriptionReader.findSubscribedAccountsByStrategyId(strategy.getId());
        outboxMessageProcessor.publishAutoTradingMessages(strategy, accounts, PositionDto.from(strategyPosition));
    }

}
