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
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.price.domain.vo.Price;
import jakarta.persistence.Convert;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void updateCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    public void updateRateAndCount(Price entryPrice, LocalDateTime entryTime) {
        ProfitRate profitRate = calculateProfitRate(entryPrice);

        if (isWin(profitRate)) {
            increaseWinCount();
            updateTotalProfitRate(profitRate);
        } else {
            increaseLossCount();
            updateTotalLossRate(profitRate);
        }

        updateAverageHoldingPeriod(entryTime);
        increaseTotalTradeCount();
        updateProfitFactor(this.rate.getTotalProfitRate(), this.rate.getTotalLossRate());
        updateWinRate();
        updateSimpleProfitRate();
        updateCompoundProfitRate(profitRate);
        updateAverageProfitRate();
    }

    private ProfitRate calculateProfitRate(Price price) {
        if (isCurrentPositionLong()) {
            return ProfitRate.of(
                price.getValue().subtract(this.currentPosition.getPrice().getValue())
                    .divide(this.currentPosition.getPrice().getValue(), 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
            );
        }
        return ProfitRate.of(
            this.currentPosition.getPrice().getValue().subtract(price.getValue())
                .divide(this.currentPosition.getPrice().getValue(), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
        );
    }

    private boolean isCurrentPositionLong() {
        return this.currentPosition.getTradingType() == TradingType.LONG;
    }

    private void increaseTotalTradeCount() {
        this.count.increaseTotalTradeCount();
    }

    private boolean isWin(ProfitRate profitRate) {
        return profitRate.getValue().compareTo(BigDecimal.ZERO) >= 0;
    }


    private void increaseWinCount() {
        this.count.increaseWinCount();
    }

    private void increaseLossCount() {
        this.count.increaseLossCount();
    }

    private void updateTotalProfitRate(ProfitRate profitRate) {
        this.rate.updateTotalProfitRate(profitRate);
    }

    private void updateTotalLossRate(ProfitRate profitRate) {
        this.rate.updateTotalLossRate(profitRate);
    }

    public void updateProfitFactor(ProfitRate totalProfitRate, ProfitRate totalLossRate) {
        if (totalLossRate == null || totalLossRate.getValue().compareTo(BigDecimal.ZERO) == 0) {
            this.profitFactor = ProfitFactor.of(BigDecimal.ZERO);
        } else {
            this.profitFactor = ProfitFactor.of(totalProfitRate.getValue().divide(totalLossRate.getValue(), 2, RoundingMode.DOWN));
        }
    }

    private void updateWinRate() {
        this.rate.updateWinRate(this.count.getWinCount(), this.count.getTotalTradeCount());
    }

    private void updateSimpleProfitRate() {
        rate.updateSimpleProfitRate();
    }

    private void updateCompoundProfitRate(ProfitRate profitRate) {
        rate.updateCompoundProfitRate(profitRate);
    }

    private void updateAverageProfitRate() {
        this.rate.updateAverageProfitRate(this.count.getTotalTradeCount());
    }

    private void updateAverageHoldingPeriod(LocalDateTime entryTime) {
        long holdingPeriod = Duration.between(entryTime, this.currentPosition.getTime()).toMinutes();

        this.averageHoldingPeriod =
            (((int) (holdingPeriod) / this.type.getTimeFrameType().getValue()) + (this.averageHoldingPeriod
                * this.count.getTotalTradeCount())) / (this.count.getTotalTradeCount() + 1);
    }

    public CoinType getCoinType() {
        return this.type.getCoinType();
    }
}
