package com.tradin.module.users.service.dto;

import com.tradin.module.strategy.domain.TradingType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubscribeStrategyDto {
    private Long id;
    private String binanceApiKey;
    private String binanceSecretKey;
    private int leverage;
    private int quantityRate;
    private TradingType tradingType;


    public static SubscribeStrategyDto of(final Long id, final String binanceApiKey,
                                          final String binanceSecretKey, final int leverage, final int quantityRate, TradingType tradingType) {
        return new SubscribeStrategyDto(id, binanceApiKey, binanceSecretKey, leverage, quantityRate, tradingType);
    }
}
