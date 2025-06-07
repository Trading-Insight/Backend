package com.tradin.module.futures.order.event;

import com.tradin.module.strategy.strategy.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AutoTradeEventDto {

    private Long strategyId;
    private Long accountId;
    private Position position;
    private String eventId;
    private Long timestamp;
} 