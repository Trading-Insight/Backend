package com.tradin.core.futuresOrder.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.TradingType;
import java.time.LocalDateTime;
import com.tradin.core.price.domain.vo.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {

    private TradingType tradingType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private Price price;

    public static PositionDto from(Position position) {
        return new PositionDto(
            position.getTradingType(),
            position.getTime(),
            position.getPrice()
        );
    }

    public Position toPosition() {
        return Position.of(
            tradingType,
            time,
            price
        );
    }

}