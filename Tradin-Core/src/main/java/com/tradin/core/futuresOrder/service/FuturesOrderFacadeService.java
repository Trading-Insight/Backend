package com.tradin.core.futuresOrder.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.balance.service.BalanceService;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.service.FuturesPositionService;
import com.tradin.core.price.domain.PriceCache;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesOrderFacadeService {
    private final FuturesOrderService futuresOrderService;
    private final BalanceService balanceService;
    private final FuturesPositionService futuresPositionService;
    private final PriceCache priceCache;

    @Transactional
    public void autoTrade(Strategy strategy, Account account, Position position) {
        try {
            closeExistingPositionIfExists(strategy, account);
            createNewPosition(strategy, account, position);
        } catch (Exception e) {
            log.error("자동매매 실패 - accountId: {}, strategyId: {}, error: {}",
                account.getId(), strategy.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 기존 포지션이 존재하는 경우 정리
     */
    private void closeExistingPositionIfExists(Strategy strategy, Account account) {
        futuresPositionService.findOpenFuturesPositionByAccountAndCoinType(account.getId(), strategy.getCoinType())
            .ifPresent(futuresPosition -> {
                           Price currentPrice = getCurrentPrice(strategy.getCoinType());

                // 1. 역방향 주문 생성
                futuresOrderService.orderReversePosition(strategy, account, futuresPosition, currentPrice);

                // 2. 포지션 정리
                futuresPositionService.closePosition(futuresPosition);

                // 3. 수익 계산 및 잔고 업데이트
                updateBalanceWithProfit(futuresPosition, currentPrice, account);
            });
    }

    /**
     * 새로운 포지션 생성
     */
    private void createNewPosition(Strategy strategy, Account account, Position position) {
        // 1. 잔고 확인 및 차감
        Balance balance = getBalance(account);
        Amount orderAmount = getOrderAmount(balance);
        updateBalanceWithMargin(balance, orderAmount);

        // 2. 현재 가격 조회
        Price currentPrice = getCurrentPrice(strategy.getCoinType());

        // 3. 주문 생성
        futuresOrderService.orderPosition(position.getTradingType(), strategy, account, orderAmount, currentPrice);

        // 4. 포지션 생성
        futuresPositionService.openPosition(strategy.getCoinType(), position.getTradingType(), orderAmount, currentPrice, account);
    }

    /**
     * 수익 계산 및 잔고 업데이트
     */
    private void updateBalanceWithProfit(FuturesPosition futuresPosition, Price currentPrice, Account account) {
        Amount profitAmount = futuresPosition.calculateProfitAmount(currentPrice);
        Balance balance = balanceService.findByAccountIdAndCoinType(account.getId(), CoinType.USDT);
        balanceService.updateBalance(balance, profitAmount);
    }

    /**
     * 잔고 조회
     */
    private Balance getBalance(Account account) {
        return balanceService.findByAccountIdAndCoinType(account.getId(), CoinType.USDT);
    }

    /**
     * 주문 금액 조회
     */
    private Amount getOrderAmount(Balance balance) {
        return Amount.of(balanceService.getUsdtAmount(balance).getValue());
    }

    /**
     * 잔고에서 마진 차감
     */
    private void updateBalanceWithMargin(Balance balance, Amount orderAmount) {
        balanceService.updateBalance(balance, orderAmount.negate());
    }

    /**
     * 현재 가격 조회
     */
    private Price getCurrentPrice(CoinType coinType) {
        return priceCache.getPrice(coinType);
    }

    /**
     * 분산락 획득 실패 시 호출되는 fallback 메서드 (포지션 정리)
     */
    public void handleClosePositionFallback(Strategy strategy, Account account) {
        log.warn("분산락 획득 실패로 인한 포지션 정리 건너뜀 - accountId: {}, strategyId: {}",
            account.getId(), strategy.getId());
    }

    /**
     * 분산락 획득 실패 시 호출되는 fallback 메서드 (포지션 생성)
     */
    public void handleCreatePositionFallback(Strategy strategy, Account account, Position position) {
        log.warn("분산락 획득 실패로 인한 포지션 생성 건너뜀 - accountId: {}, strategyId: {}",
            account.getId(), strategy.getId());
    }
}
