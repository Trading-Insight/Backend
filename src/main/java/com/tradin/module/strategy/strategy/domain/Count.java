package com.tradin.module.strategy.strategy.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Count {

    @Column(nullable = false)
    private int totalTradeCount;

    @Column(nullable = false)
    private int winCount;

    @Column(nullable = false)
    private int lossCount;

    @Builder
    private Count(int totalTradeCount, int winCount, int lossCount) {
        this.totalTradeCount = totalTradeCount;
        this.winCount = winCount;
        this.lossCount = lossCount;
    }

    public static Count of(int totalTradeCount, int winCount, int lossCount) {
        return Count.builder()
            .totalTradeCount(totalTradeCount)
            .winCount(winCount)
            .lossCount(lossCount)
            .build();
    }

    public void increaseTotalTradeCount() {
        this.totalTradeCount++;
    }

    public void increaseWinCount() {
        this.winCount++;
    }

    public void increaseLossCount() {
        this.lossCount++;
    }
}
