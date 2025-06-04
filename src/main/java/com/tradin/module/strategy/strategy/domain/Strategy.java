package com.tradin.module.strategy.strategy.domain;

import com.tradin.common.jpa.AuditTime;
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

    @Column
    private double profitFactor; //TODO - BigDecimal로 변경

    @Column
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

    public void updateRateAndCount(double entryPrice, LocalDateTime entryTime) {
        double profitRate = calculateProfitRate(entryPrice); //TODO - processor로 이동

        if (isWin(profitRate)) {
            increaseWinCount();
            updateTotalProfitRate(profitRate);
        } else {
            increaseLossCount();
            updateTotalLossRate(profitRate);
        }

        updateAverageHoldingPeriod(entryTime);
        increaseTotalTradeCount();
        updateProfitFactor();
        updateWinRate();
        updateSimpleProfitRate();
        updateCompoundProfitRate(profitRate);
        updateAverageProfitRate();
    }

    private double calculateProfitRate(double price) {
        if (isCurrentPositionLong()) {
            return ((price - this.currentPosition.getPrice()) / this.currentPosition.getPrice()) * 100;
        }

        return ((this.currentPosition.getPrice() - price) / this.currentPosition.getPrice())
            * 100;
    }

    private boolean isCurrentPositionLong() {
        return this.currentPosition.getTradingType() == TradingType.LONG;
    }

    private void increaseTotalTradeCount() {
        this.count.increaseTotalTradeCount();
    }

    private boolean isWin(double profitRate) {
        return profitRate >= 0;
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
