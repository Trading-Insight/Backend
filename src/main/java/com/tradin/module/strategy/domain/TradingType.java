package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TradingType {
    LONG("매수"),
    SHORT("매도"),
    NONE("무"),
    BOTH("매수&매도");

    private final String value;
}