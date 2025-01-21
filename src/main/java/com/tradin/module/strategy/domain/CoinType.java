package com.tradin.module.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CoinType {
    BITCOIN("비트코인");

    private final String value;
}
