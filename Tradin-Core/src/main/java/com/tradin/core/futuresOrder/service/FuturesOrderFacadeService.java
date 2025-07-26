package com.tradin.core.futuresOrder.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.balance.service.BalanceService;
import com.tradin.core.futuresPosition.service.FuturesPositionService;
import com.tradin.core.price.domain.PriceCache;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FuturesOrderFacadeService {
    private final FuturesOrderService futuresOrderService;
    private final BalanceService balanceService;
    private final FuturesPositionService futuresPositionService;

    private final PriceCache priceCache;

    @Transactional
    public void autoTrade(Strategy strategy, Account account, Position position) {
        // 기존 포지션이 있다면 정리
        futuresPositionService.findOpenFuturesPositionByAccountAndCoinType(account.getId(), strategy.getCoinType())
            .ifPresent(futuresPosition -> {
                Price currentPrice = getCurrentPrice(strategy.getCoinType());
                Amount profitAmount = futuresOrderService.calculateProfitAmount(futuresPosition, currentPrice);
                Balance balance = balanceService.findByAccountIdAndCoinType(account.getId(), CoinType.USDT);
                balanceService.updateBalance(balance, Amount.of(profitAmount.getValue()));
            });
        Balance balance = balanceService.findByAccountIdAndCoinType(account.getId(), CoinType.USDT);
        Amount orderAmount = Amount.of(balanceService.getUsdtAmount(balance).getValue());
        Price currentPrice = getCurrentPrice(strategy.getCoinType());
        balanceService.updateBalance(balance, Amount.of(orderAmount.getValue().negate()));
        futuresOrderService.orderPosition(position.getTradingType(), strategy, account, Amount.of(orderAmount.getValue()), currentPrice);
        futuresPositionService.openPosition(strategy.getCoinType(), position.getTradingType(), Amount.of(orderAmount.getValue()), currentPrice, account);
    }

    private Price getCurrentPrice(CoinType coinType) {
        return priceCache.getPrice(coinType);
    }
}
