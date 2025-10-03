package com.tradin.core.futuresOrder.service;

import static java.lang.Thread.sleep;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesOrderFacadeService {
    private final FuturesOrderService futuresOrderService;
    private final BalanceService balanceService;
    private final FuturesPositionService futuresPositionService;

    @DistributedLock(
        key = "'asset-lock:' + #account.id + ':USDT'",
        fallbackMethod = "handleAssetLockFallback"
    )
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

                // 1. 역방향 주문 생성
                futuresOrderService.orderReversePosition(strategy, account, futuresPosition);

                // 2. 포지션 정리
                futuresPositionService.closePosition(futuresPosition);

                // 3. 수익 계산
                Amount profitAmount = futuresPositionService.calculateProfitAmount(futuresPosition);

                // 4. 수익 정산
                Balance balance = balanceService.findUsdtBalanceByAccount(account.getId());
                balanceService.settleProfit(balance, futuresPosition.getMargin(), profitAmount);
            });
    }

    /**
     * 새로운 포지션 생성
     */
    private void createNewPosition(Strategy strategy, Account account, Position position) {
        // 1. 잔고 확인 및 차감
        Balance balance = balanceService.findUsdtBalanceByAccount(account.getId());
        Amount margin = balanceService.getUsdtAmount(balance);
        balanceService.subtractMargin(balance, margin);

        // 2. 주문 생성
        futuresOrderService.orderPosition(position.getTradingType(), strategy, account, margin);

        // 3. 포지션 생성
        futuresPositionService.openPosition(strategy.getCoinType(), position.getTradingType(), margin, account);
    }


    /**
     * 자동매매 처리 시 분산락 획득 실패하면 호출되는 fallback 메서드
     */
    public void handleAssetLockFallback(Strategy strategy, Account account, Position position) {
        log.warn("자동매매 분산락 획득 실패 - accountId: {}, strategyId: {}, position: {} ",
            account.getId(), strategy.getId(), position);
    }
}
