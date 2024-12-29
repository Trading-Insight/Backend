package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StrategyType {
    FUTURE("선물"),
    SPOT("현물");

    private final String value;
}
