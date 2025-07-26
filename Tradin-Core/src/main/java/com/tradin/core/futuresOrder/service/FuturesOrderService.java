package com.tradin.core.futuresOrder.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresOrder.domain.FuturesOrder;
import com.tradin.core.futuresOrder.domain.OrderStatus;
import com.tradin.core.futuresOrder.domain.repository.FuturesOrderRepository;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuturesOrderService {
    private final FuturesOrderRepository futuresOrderRepository;

    public FuturesOrder orderPosition(TradingType tradingType, Strategy strategy, Account account, Amount amount, Price currentPrice) {
        FuturesOrder futuresOrder = FuturesOrder.of(tradingType, currentPrice, amount, OrderStatus.FILLED, account, strategy);
        return futuresOrderRepository.save(futuresOrder);
    }

    public FuturesOrder orderReversePosition(Strategy strategy, Account account, FuturesPosition futuresPosition, Price currentPrice) {
        TradingType reverseTradingType = futuresPosition.isPositionLong() ? TradingType.SHORT : TradingType.LONG;
        return orderPosition(reverseTradingType, strategy, account, Amount.of(futuresPosition.getAmount().getValue()), currentPrice);
    }

    public Amount calculateProfitAmount(FuturesPosition futuresPosition, Price currentPrice) {
        if (futuresPosition.isPositionLong()) {
            return Amount.of(currentPrice.getValue().subtract(futuresPosition.getEntryPrice().getValue()));
        } else {
            return Amount.of(futuresPosition.getEntryPrice().getValue().subtract(currentPrice.getValue()));
        }
    }
}
