package com.tradin.module.futures.order.implement;

import com.tradin.module.futures.order.domain.FuturesOrder;
import com.tradin.module.futures.order.domain.OrderStatus;
import com.tradin.module.futures.order.domain.repository.FuturesOrderRepository;
import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.futures.position.implement.FuturesPositionProcessor;
import com.tradin.module.futures.position.implement.FuturesPositionReader;
import com.tradin.module.futures.price.domain.PriceCache;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.implement.AccountReader;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.balance.implement.BalanceProcessor;
import com.tradin.module.users.balance.implement.BalanceReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuturesOrderProcessor {

    private final PriceCache priceCache;
    private final AccountReader accountReader;
    private final BalanceReader balanceReader;
    private final BalanceProcessor balanceProcessor;
    private final FuturesPositionReader futuresPositionReader;
    private final FuturesPositionProcessor futuresPositionProcessor;

    private final FuturesOrderRepository futuresOrderRepository;

    /**
     * 1. 기존 포지션 조회 (신규 주문인 경우 SKIP) 2. 반대 매매 주문 3. 포지션 청산 (시장가) 4. USDT 수익 정산
     */
    public void closeExistPosition(Strategy strategy, Account account) {
        futuresPositionReader.findOpenFuturesPositionByAccountAndCoinTypeForUpdate(account.getId(), strategy.getCoinType())
            .ifPresent(futuresPosition -> {
                BigDecimal currentPrice = getCurrentPrice(strategy.getCoinType());
                
                orderReversePosition(strategy, account, futuresPosition, currentPrice);
                closeExistPosition(account, futuresPosition);
                settleUsdtProfit(
                    balanceReader.findByAccountIdAndCoinTypeForUpdate(account.getId(), CoinType.USDT),
                    calculateProfitAmount(futuresPosition, currentPrice)
                );
            });
    }

    /**
     * 1. USDT 잔고 조회 2. 마진 차감 3. 신규 포지션 주문 4. 포지션 오픈
     */
    public void openNewPosition(Strategy strategy, Account account, Position strategyPosition) {
        Balance balance = balanceReader.findByAccountIdAndCoinTypeForUpdate(account.getId(), CoinType.USDT);
        BigDecimal orderAmount = balanceReader.getUsdtAmount(balance);
        BigDecimal currentPrice = getCurrentPrice(strategy.getCoinType());

        reduceMargin(balance, orderAmount);
        orderPosition(strategyPosition.getTradingType(), strategy, account, orderAmount, currentPrice);
        openPosition(strategy, account, strategyPosition, orderAmount, currentPrice);
    }

    private FuturesOrder orderPosition(TradingType tradingType, Strategy strategy, Account account, BigDecimal amount, BigDecimal currentPrice) {
        FuturesOrder futuresOrder = FuturesOrder.of(tradingType, currentPrice, amount, OrderStatus.FILLED, account, strategy);
        return futuresOrderRepository.save(futuresOrder);
    }

    private void orderReversePosition(Strategy strategy, Account account, FuturesPosition futuresPosition, BigDecimal currentPrice) {
        if (futuresPosition.isPositionLong()) {
            orderPosition(TradingType.SHORT, strategy, account, futuresPosition.getAmount(), currentPrice);
        } else if (isPositionShort(futuresPosition)) {
            orderPosition(TradingType.LONG, strategy, account, futuresPosition.getAmount(), currentPrice);
        }
    }

    private void closeExistPosition(Account account, FuturesPosition futuresPosition) {
        futuresPositionProcessor.closePosition(account, futuresPosition);
    }

    private FuturesPosition openPosition(Strategy strategy, Account account, Position strategyPosition, BigDecimal amount, BigDecimal currentPrice) {
        return futuresPositionProcessor.openPosition(
            strategy.getCoinType(),
            strategyPosition.getTradingType(),
            amount,
            currentPrice,
            account
        );
    }

    private void settleUsdtProfit(Balance balance, BigDecimal amount) {
        balanceProcessor.updateBalance(balance, amount);
    }

    private void reduceMargin(Balance balance, BigDecimal margin) {
        balanceProcessor.updateBalance(balance, margin.negate());
    }

    private BigDecimal calculateProfitAmount(FuturesPosition position, BigDecimal currentPrice) {
        if (position.isPositionLong()) {
            return currentPrice.subtract(position.getEntryPrice())
                .multiply(position.getAmount())
                .divide(position.getEntryPrice(), 2, RoundingMode.DOWN).add(position.getMargin());
        }

        return position.getEntryPrice().subtract(currentPrice)
            .multiply(position.getAmount())
            .divide(position.getEntryPrice(), 2, RoundingMode.DOWN).add(position.getMargin());
    }


    private boolean isPositionShort(FuturesPosition position) {
        return position.isPositionShort();
    }


    private BigDecimal getCurrentPrice(CoinType coinType) {
        return priceCache.getPrice(coinType);
    }

}
