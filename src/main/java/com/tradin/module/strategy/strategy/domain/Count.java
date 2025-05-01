package com.tradin.module.strategy.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

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
