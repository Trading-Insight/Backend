package com.tradin.common.fixture;

import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import java.math.BigDecimal;

public class FuturesPositionFixture {

    /**
     * 기본 FuturesPosition 생성 (LONG BTC)
     */
    public static FuturesPosition createDefaultPosition() {
        return FuturesPosition.of(
            CoinType.BTC,
            TradingType.LONG,
            new BigDecimal("0.1000"),
            new BigDecimal("50000.00"),
            AccountFixture.createDefaultAccount()
        );
    }
}
