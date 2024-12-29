package com.tradin.module.history.controller.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class BackTestRequestDto {
    @NotNull(message = "StrategyId must not be null")
    private long id;

    @NotBlank(message = "StrategyName must not be blank")
    private String name;

    @NotNull(message = "StartDate must not be null")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "시작 연,월,일", example = "2021-01-01")
    private LocalDate startDate;

    @NotNull(message = "EndDate must not be null")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "종료 연,월,일", example = "2021-01-01")
    private LocalDate endDate;

    @Schema(description = "매매 타입", example = "LONG")
    @NotNull(message = "TradingType must not be null")
    private TradingType tradingType;

    public BackTestDto toServiceDto() {
        return BackTestDto.of(id, name, startDate, endDate, tradingType);
    }
}
