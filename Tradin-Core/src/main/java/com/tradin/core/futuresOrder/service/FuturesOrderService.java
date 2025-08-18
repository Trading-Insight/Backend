package com.tradin.core.futuresOrder.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresOrder.domain.FuturesOrder;
import com.tradin.core.futuresOrder.domain.OrderStatus;
import com.tradin.core.futuresOrder.domain.repository.FuturesOrderRepository;
import com.tradin.core.price.domain.PriceCache;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuturesOrderService {
    private final PriceCache priceCache;

    private final FuturesOrderRepository futuresOrderRepository;

    public void orderPosition(TradingType tradingType, Strategy strategy, Account account, Amount amount) {
        FuturesOrder futuresOrder = FuturesOrder.of(tradingType, priceCache.getPrice(strategy.getCoinType()), amount, OrderStatus.FILLED, account, strategy);
        futuresOrderRepository.save(futuresOrder);
    }

    public void orderReversePosition(Strategy strategy, Account account, FuturesPosition futuresPosition) {
        TradingType reverseTradingType = futuresPosition.isPositionLong() ? TradingType.SHORT : TradingType.LONG;
        orderPosition(reverseTradingType, strategy, account, Amount.of(futuresPosition.getAmount().getValue()));
    }
}
