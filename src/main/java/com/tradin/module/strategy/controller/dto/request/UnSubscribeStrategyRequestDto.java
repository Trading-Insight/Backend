package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.strategy.service.dto.UnSubscribeStrategyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnSubscribeStrategyRequestDto {
    @NotNull(message = "StrategyId must not be null")
    private long id;

    @NotNull(message = "isPositionClose must not be null")
    private boolean isPositionClose;

    public UnSubscribeStrategyDto toServiceDto() {
        return UnSubscribeStrategyDto.of(id, isPositionClose);
    }
}
