package com.tradin.module.history.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StrategyInfoDto {
    private final Long id;
    private final String name;
    private final double compoundProfitRate;
    private final double winRate;
    private final double profitFactor;
    private final int totalTradeCount;
    private final double averageProfitRate;

    @Builder
    public StrategyInfoDto(Long id, String name, double compoundProfitRate, double winRate, double profitFactor, int totalTradeCount, double averageProfitRate) {
        this.id = id;
        this.name = name;
        this.compoundProfitRate = compoundProfitRate;
        this.winRate = winRate;
        this.profitFactor = profitFactor;
        this.totalTradeCount = totalTradeCount;
        this.averageProfitRate = averageProfitRate;
    }

    public static StrategyInfoDto of(Long id, String name, double compoundProfitRate, double winRate, double profitFactor, int totalTradeCount, double averageProfitRate) {
        return StrategyInfoDto.builder()
                .id(id)
                .name(name)
                .compoundProfitRate(compoundProfitRate)
                .winRate(winRate)
                .profitFactor(profitFactor)
                .totalTradeCount(totalTradeCount)
                .averageProfitRate(averageProfitRate)
                .build();
    }
}
