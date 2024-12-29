package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Type {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StrategyType strategyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Builder
    private Type(StrategyType strategyType, CoinType coinType) {
        this.strategyType = strategyType;
        this.coinType = coinType;
    }
}
