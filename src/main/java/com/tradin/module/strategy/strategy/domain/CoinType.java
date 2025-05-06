package com.tradin.module.strategy.strategy.domain;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CoinType {
    BTC("비트코인"),
    USDT("테더"),
    ETH("이더리움");
    private final String value;

    public boolean isBitcoin() {
        return this == BTC;
    }

    public boolean isEthereum() {
        return this == ETH;
    }

    public boolean isUsdt() {
        return this == USDT;
    }

    public boolean isMatch(CoinType coinType) {
        return this == coinType;
    }

    public static Optional<CoinType> fromSymbol(String symbol) {
        if (symbol == null) {
            return Optional.empty();
        }
        if (symbol.startsWith("BTCUSDT")) {
            return Optional.of(BTC);
        }
        if (symbol.startsWith("ETHUSDT")) {
            return Optional.of(ETH);
        }
        return Optional.empty();
    }
}
