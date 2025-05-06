package com.tradin.module.strategy.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum TimeFrameType {
    ONE_HOUR(60),
    FOUR_HOUR(2400),
    SIX_HOUR(3600),
    TWELVE_HOUR(7200),
    ONE_DAY(14400);

    private final int value;
}
