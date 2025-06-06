package com.tradin.common.fixture;

import com.tradin.module.futures.order.domain.FuturesOrder;
import com.tradin.module.futures.order.domain.OrderStatus;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.account.domain.Account;
import java.math.BigDecimal;

public class FuturesOrderFixture {

    /**
     * 기본 FuturesOrder 생성 (OPEN 상태)
     */
    public static FuturesOrder createDefaultOrder() {
        return FuturesOrder.of(
            TradingType.LONG,
            new BigDecimal("50000.00"),
            new BigDecimal("0.1000"),
            OrderStatus.OPEN,
            AccountFixture.createDefaultAccount(),
            StrategyFixture.createDefaultStrategy()
        );
    }

    /**
     * 커스텀 FuturesOrder 생성
     */
    public static FuturesOrder createOrder(Account account, Strategy strategy, TradingType tradingType, BigDecimal price, BigDecimal amount, OrderStatus status) {
        return FuturesOrder.of(tradingType, price, amount, status, account, strategy);
    }

}
