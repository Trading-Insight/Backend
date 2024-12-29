package com.tradin.module.users.service.dto;

import com.tradin.module.strategy.domain.TradingType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ChangeMetadataDto {
    private final int leverage;
    private final int quantityRate;
    private final TradingType tradingTypes;

    public static ChangeMetadataDto of(final int leverage, final int quantityRate, final TradingType tradingType) {
        return new ChangeMetadataDto(leverage, quantityRate, tradingType);
    }
}
