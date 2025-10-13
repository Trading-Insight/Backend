package com.tradin.core.strategy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TradingType {
    LONG("매수"),
    SHORT("매도"),
    NONE("무"),
    BOTH("매수&매도");

    private final String value;

    @JsonIgnore
    public boolean isLong() {
        return this == LONG;
    }

    @JsonIgnore
    public boolean isShort() {
        return this == SHORT;
    }
}