package com.tradin.module.history.service.dto;

import com.tradin.module.strategy.domain.TradingType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

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
