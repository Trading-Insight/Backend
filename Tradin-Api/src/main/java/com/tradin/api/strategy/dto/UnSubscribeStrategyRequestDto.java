package com.tradin.api.strategy.dto;

import com.tradin.core.strategy.service.dto.UnSubscribeStrategyDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
