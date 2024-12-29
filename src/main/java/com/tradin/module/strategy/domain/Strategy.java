package com.tradin.module.strategy.domain;

import com.tradin.common.jpa.AuditTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

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
    private Type type;

    @Embedded
    private Rate rate;

    @Embedded
    private Count count;

    @Embedded
    private Position currentPosition;

    @Column(nullable = false)
    private double profitFactor;

    @Column(nullable = false)
    private int averageHoldingPeriod;

    @Builder
    private Strategy(String name, Type type, Rate rate, Count count, Position currentPosition, double profitFactor, int averageHoldingPeriod) {
        this.name = name;
        this.type = type;
        this.rate = rate;
        this.count = count;
        this.currentPosition = currentPosition;
        this.profitFactor = profitFactor;
        this.averageHoldingPeriod = averageHoldingPeriod;
    }

    public static Strategy of(String name, Type type, Rate rate, Count count, Position currentPosition, double profitFactor, int averageHoldingPeriod) {
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

    public void updateMetaData(Position position) {
        double profitRate = calculateProfitRate(position);

        if (isWin(profitRate)) {
            increaseWinCount();
            updateTotalProfitRate(profitRate);
        } else {
            increaseLossCount();
            updateTotalLossRate(profitRate);
        }

        increaseTotalTradeCount();
        updateProfitFactor();
        updateWinRate();
        updateSimpleProfitRate();
        updateCompoundProfitRate(profitRate);
        updateCurrentPosition(position);
        updateAverageProfitRate();
    }

    public boolean isLongPosition() {
        return this.currentPosition.getTradingType() == TradingType.LONG;
    }

    public boolean isShortPosition() {
        return this.currentPosition.getTradingType() == TradingType.SHORT;
    }

    private void increaseTotalTradeCount() {
        this.count.increaseTotalTradeCount();
    }

    private boolean isWin(double profitRate) {
        return profitRate > 0;
    }

    private boolean isCurrentPositionLong() {
        return this.currentPosition.getTradingType() == TradingType.LONG;
    }

    private void increaseWinCount() {
        this.count.increaseWinCount();
    }

    private void increaseLossCount() {
        this.count.increaseLossCount();
    }

    private void updateTotalProfitRate(double profitRate) {
        this.rate.updateTotalProfitRate(profitRate);
    }

    private void updateTotalLossRate(double profitRate) {
        this.rate.updateTotalLossRate(profitRate);
    }

    private void updateProfitFactor() {
        this.profitFactor = this.rate.getTotalProfitRate() / this.rate.getTotalLossRate();
    }

    private void updateWinRate() {
        this.rate.updateWinRate(this.count.getWinCount(), this.count.getTotalTradeCount());
    }

    private void updateSimpleProfitRate() {
        rate.updateSimpleProfitRate();
    }

    private void updateCompoundProfitRate(double profitRate) {
        rate.updateCompoundProfitRate(profitRate);
    }

    private void updateAverageProfitRate() {
        this.rate.updateAverageProfitRate(this.count.getTotalTradeCount());
    }

    private double calculateProfitRate(Position position) {
        if (isCurrentPositionLong()) {
            return ((double) (position.getPrice() - this.currentPosition.getPrice()) / this.currentPosition.getPrice())
                    * 100;
        }

        return ((double) (this.currentPosition.getPrice() - position.getPrice()) / this.currentPosition.getPrice())
                * 100;
    }
}
