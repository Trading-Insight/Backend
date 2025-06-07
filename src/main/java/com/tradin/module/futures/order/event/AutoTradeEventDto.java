package com.tradin.module.futures.order.event;

import com.tradin.module.futures.order.event.dto.PositionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AutoTradeEventDto {

    private Long strategyId;
    private Long accountId;
    private PositionDto position;
    private String eventId;
    private Long timestamp;


    public static AutoTradeEventDto of(Long strategyId, Long accountId, PositionDto positionDto, String eventId, Long timestamp) {
        return new AutoTradeEventDto(
            strategyId,
            accountId,
            positionDto,
            eventId,
            timestamp
        );
    }
} 