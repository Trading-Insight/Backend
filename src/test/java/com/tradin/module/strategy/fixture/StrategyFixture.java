package com.tradin.module.strategy.fixture;

import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.Count;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Rate;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.domain.StrategyType;
import com.tradin.module.strategy.strategy.domain.TimeFrameType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.strategy.strategy.domain.Type;
import java.time.LocalDateTime;

public class StrategyFixture {

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
            .profitFactor(1.5)
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
            .winningRate(0.6)
            .simpleProfitRate(0.1)
            .compoundProfitRate(0.15)
            .totalProfitRate(0.2)
            .totalLossRate(0.05)
            .averageProfitRate(0.08)
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
        return Position.builder()
            .tradingType(TradingType.LONG)
            .time(LocalDateTime.now())
            .price(50000)
            .build();
    }
}