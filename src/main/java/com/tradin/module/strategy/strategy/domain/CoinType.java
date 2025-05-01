package com.tradin.module.strategy.strategy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CoinType {
    BITCOIN("비트코인"),
    USDT("테더"),
    ETHEREUM("이더리움");
    private final String value;
}
