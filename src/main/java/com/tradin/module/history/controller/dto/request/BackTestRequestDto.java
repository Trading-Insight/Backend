package com.tradin.module.history.controller.dto.request;

import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "백테스트 실행 DTO")
public record BackTestRequestDto(
    @NotNull(message = "StrategyId must not be null") long id,

    @NotBlank(message = "StrategyName must not be blank") String name,

    @NotNull(message = "StartDate must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 연,월,일", example = "2021-01-01") LocalDate startDate,

    @NotNull(message = "EndDate must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @Schema(description = "종료 연,월,일", example = "2021-01-01") LocalDate endDate,
    @NotNull(message = "TradingType must not be null")

    @Schema(description = "매매 타입", example = "LONG") TradingType tradingType
) {

    public BackTestDto toServiceDto() {
        return BackTestDto.of(id, name, startDate, endDate, tradingType);
    }
}
