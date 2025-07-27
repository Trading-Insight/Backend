package com.tradin.core.strategy.domain;

import com.tradin.core.common.jpa.AuditTime;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.common.converter.ProfitFactorConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.price.domain.vo.Price;
import jakarta.persistence.Convert;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_strategy_name", columnList = "name"),
    @Index(name = "idx_strategy_type_strategy", columnList = "strategy_type"),
    @Index(name = "idx_strategy_type_coin", columnList = "coin_type"),
    @Index(name = "idx_strategy_type_timeframe", columnList = "time_frame_type"),
    @Index(name = "idx_strategy_profit_factor", columnList = "profit_factor"),
    @Index(name = "idx_strategy_created_at", columnList = "created_at")
})
public class Strategy extends AuditTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Type type;

    @Embedded
    private Rate rate;

    @Embedded
    private Count count;

    @Embedded
    private Position currentPosition;

    @Convert(converter = ProfitFactorConverter.class)
    @Column(nullable = false, precision = 20, scale = 2)
    private ProfitFactor profitFactor;

    @Column
    private int averageHoldingPeriod;

    @Builder
    private Strategy(String name, Type type, Rate rate, Count count, Position currentPosition, ProfitFactor profitFactor, int averageHoldingPeriod) {
        this.name = name;
        this.type = type;
        this.rate = rate;
        this.count = count;
        this.currentPosition = currentPosition;
        this.profitFactor = profitFactor == null ? ProfitFactor.of(null) : profitFactor;
        this.averageHoldingPeriod = averageHoldingPeriod;
    }

    public static Strategy of(String name, Type type, Rate rate, Count count, Position currentPosition, ProfitFactor profitFactor, int averageHoldingPeriod) {
        return Strategy.builder()
            .name(name)
            .type(type)
            .rate(rate)
            .count(count)
            .currentPosition(currentPosition)
            .profitFactor(profitFactor)
            .averageHoldingPeriod(averageHoldingPeriod)
            .build();
    }

    // 기본적인 상태 변경 메서드들
    public void updateCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    public void updateProfitFactor(ProfitFactor profitFactor) {
        this.profitFactor = profitFactor;
    }

    public void updateAverageHoldingPeriod(int averageHoldingPeriod) {
        this.averageHoldingPeriod = averageHoldingPeriod;
    }

    public void increaseTotalTradeCount() {
        this.count.increaseTotalTradeCount();
    }

    public void increaseWinCount() {
        this.count.increaseWinCount();
    }

    public void increaseLossCount() {
        this.count.increaseLossCount();
    }

    public void updateTotalProfitRate(ProfitRate profitRate) {
        this.rate.updateTotalProfitRate(profitRate);
    }

    public void updateTotalLossRate(ProfitRate profitRate) {
        this.rate.updateTotalLossRate(profitRate);
    }

    public void updateWinRate() {
        this.rate.updateWinRate(this.count.getWinCount(), this.count.getTotalTradeCount());
    }

    public void updateSimpleProfitRate() {
        this.rate.updateSimpleProfitRate();
    }

    public void updateCompoundProfitRate(ProfitRate profitRate) {
        this.rate.updateCompoundProfitRate(profitRate);
    }

    public void updateAverageProfitRate() {
        this.rate.updateAverageProfitRate(this.count.getTotalTradeCount());
    }

    public CoinType getCoinType() {
        return this.type.getCoinType();
    }

    public boolean isCurrentPositionLong() {
        return this.currentPosition.getTradingType() == TradingType.LONG;
    }
}
