package com.tradin.core.fixture;


import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
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
