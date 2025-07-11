package com.tradin.module.strategy.strategy.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Position {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;

    @Column(nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    @Column(nullable = false)
    private int price;  //TODO - BigDecimal로 변경

    @Builder
    private Position(TradingType tradingType, LocalDateTime time, int price) {
        this.tradingType = tradingType;
        this.time = time;
        this.price = price;
    }

    public static Position of(TradingType tradingType, LocalDateTime time, int price) {
        return Position.builder()
            .tradingType(tradingType)
            .time(time)
            .price(price)
            .build();
    }

    public boolean isLong() {
        return tradingType == TradingType.LONG;
    }

    public boolean isShort() {
        return tradingType == TradingType.SHORT;
    }
}
