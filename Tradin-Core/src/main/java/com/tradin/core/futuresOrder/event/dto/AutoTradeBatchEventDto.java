package com.tradin.core.futuresOrder.event.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AutoTradeBatchEventDto {

    private Long strategyId;
    private List<Long> accountIds;
    private PositionDto position;
    private String eventId;
    private Long timestamp;

    public static AutoTradeBatchEventDto of(Long strategyId, List<Long> accountIds, PositionDto positionDto, String eventId, Long timestamp) {
        return new AutoTradeBatchEventDto(
            strategyId,
            accountIds,
            positionDto,
            eventId,
            timestamp
        );
    }
} 