package com.tradin.core.history.service.dto;

import com.tradin.core.strategy.domain.TradingType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BackTestDto {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private TradingType tradingType;

    public static BackTestDto of(Long id, String name, LocalDate startDate, LocalDate endDate, TradingType tradingType) {
        return new BackTestDto(id, name, startDate, endDate, tradingType);
    }
}
