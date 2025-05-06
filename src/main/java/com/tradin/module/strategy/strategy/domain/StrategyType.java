package com.tradin.module.strategy.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum StrategyType {
    FUTURE("선물"),
    SPOT("현물");

    private final String value;
}
