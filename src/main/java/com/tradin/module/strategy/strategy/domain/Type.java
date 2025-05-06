package com.tradin.module.strategy.strategy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Type {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyType strategyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeFrameType timeFrameType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Builder
    private Type(StrategyType strategyType, CoinType coinType, TimeFrameType timeFrameType) {
        this.strategyType = strategyType;
        this.coinType = coinType;
        this.timeFrameType = timeFrameType;
    }

    public static Type of(StrategyType strategyType, CoinType coinType, TimeFrameType timeFrameType) {
        return Type.builder()
            .strategyType(strategyType)
            .coinType(coinType)
            .timeFrameType(timeFrameType)
            .build();
    }
}
