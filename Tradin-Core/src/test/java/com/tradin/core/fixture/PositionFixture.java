package com.tradin.core.fixture;

import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.TradingType;
import java.time.LocalDateTime;

public class PositionFixture {

    public static Position get() {
        return createDefaultPosition();
    }

    public static Position getDifferent(Position position) {
        return Position.builder()
            .tradingType(position.getTradingType() == TradingType.LONG ? TradingType.SHORT : TradingType.LONG)
            .time(LocalDateTime.now())
            .price(Price.from(50000))
            .build();
    }

    /**
     * 기본 Position 생성
     */
    public static Position createDefaultPosition() {
        return Position.builder()
            .tradingType(TradingType.LONG)
            .time(LocalDateTime.now())
            .price(Price.from(50000))
            .build();
    }
}
