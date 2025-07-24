package com.tradin.core.fixture;

import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Count;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Rate;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.StrategyType;
import com.tradin.core.strategy.domain.TimeFrameType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.domain.Type;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.tradin.core.price.domain.vo.Price;

public class StrategyFixture {

    public static Strategy get() {
        return createDefaultStrategy();
    }

    /**
     * 기본 Strategy 생성
     */
    public static Strategy createDefaultStrategy() {
        return Strategy.builder()
            .name("Test Strategy")
            .type(createDefaultType())
            .rate(createDefaultRate())
            .count(createDefaultCount())
            .currentPosition(createDefaultPosition())
            .profitFactor(new BigDecimal("1.50"))
            .averageHoldingPeriod(30)
            .build();
    }

    /**
     * 기본 Type 생성
     */
    private static Type createDefaultType() {
        return Type.of(StrategyType.FUTURE, CoinType.BTC, TimeFrameType.ONE_HOUR);
    }

    /**
     * 기본 Rate 생성
     */
    private static Rate createDefaultRate() {
        return Rate.builder()
            .winningRate(new BigDecimal("0.60"))
            .simpleProfitRate(new BigDecimal("0.10"))
            .compoundProfitRate(new BigDecimal("0.15"))
            .totalProfitRate(new BigDecimal("0.20"))
            .totalLossRate(new BigDecimal("0.05"))
            .averageProfitRate(new BigDecimal("0.08"))
            .build();
    }

    /**
     * 기본 Count 생성
     */
    private static Count createDefaultCount() {
        return Count.builder()
            .totalTradeCount(100)
            .winCount(60)
            .lossCount(40)
            .build();
    }

    /**
     * 기본 Position 생성
     */
    private static Position createDefaultPosition() {
        return Position.of(
            TradingType.LONG,
            LocalDateTime.now(),
            Price.of(new BigDecimal("10000.00"))
        );
    }
}