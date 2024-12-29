package com.tradin.module.strategy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnSubscribeStrategyDto {
    private final long id;
    private final boolean isPositionClose;

    public static UnSubscribeStrategyDto of(long id, boolean isPositionClose) {
        return new UnSubscribeStrategyDto(id, isPositionClose);
    }
}
