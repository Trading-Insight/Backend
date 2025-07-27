package com.tradin.core.fixture;

import com.tradin.core.account.domain.Account;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresOrder.domain.FuturesOrder;
import com.tradin.core.futuresOrder.domain.OrderStatus;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.price.domain.vo.Price;
import java.math.BigDecimal;

public class FuturesOrderFixture {

    /**
     * 기본 FuturesOrder 생성 (OPEN 상태)
     */
    public static FuturesOrder createDefaultOrder() {
        return FuturesOrder.of(
            TradingType.LONG,
            Price.of(new BigDecimal("50000.00")),
            Amount.of(new BigDecimal("0.1000")),
            OrderStatus.OPEN,
            AccountFixture.createDefaultAccount(),
            StrategyFixture.createDefaultStrategy()
        );
    }

    /**
     * 커스텀 FuturesOrder 생성
     */
    public static FuturesOrder createOrder(Account account, Strategy strategy, TradingType tradingType, Price price, Amount amount, OrderStatus status) {
        return FuturesOrder.of(tradingType, price, amount, status, account, strategy);
    }

}
