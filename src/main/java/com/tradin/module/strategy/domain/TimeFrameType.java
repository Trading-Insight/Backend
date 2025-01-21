package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum TimeFrameType {
    ONE_HOUR("1시간"),
    FOUR_HOUR("4시간"),
    SIX_HOUR("6시간"),
    TWELVE_HOUR("12시간"),
    ONE_DAY("1일");

    private final String value;
}
