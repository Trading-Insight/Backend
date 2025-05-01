package com.tradin.module.users.users.controller.dto.request;

import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.users.users.service.dto.ChangeMetadataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMetadataRequestDto {
    @NotNull(message = "Leverage must not be null")
    @Min(value = 1, message = "레버리지는 최소 1이어야 합니다.")
    @Max(value = 125, message = "레버리지는 최대 125까지 가능합니다.")
    @Schema(description = "레버리지", example = "1 ~ 125")
    private int leverage;

    @NotNull(message = "QuantityRate must not be null")
    @Min(value = 1, message = "수량 비율은 최소 1이어야 합니다.")
    @Max(value = 100, message = "수량 비율은 최대 100까지 가능합니다.")
    private int quantityRate;

    @Schema(description = "매매 타입", example = "LONG, SHORT, BOTH")
    @NotNull(message = "TradingType must not be null")
    private TradingType tradingType;

    public ChangeMetadataDto toServiceDto() {
        return ChangeMetadataDto.of(leverage, quantityRate, tradingType);
    }
}
