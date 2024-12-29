package com.tradin.module.feign.client.dto.binance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentPositionInfoDto {
    private String entryPrice;
    private String marginType;
    private String isAutoAddMargin;
    private String isolatedMargin;
    private String leverage;
    private String liquidationPrice;
    private String markPrice;
    private String maxNotionalValue;
    private String positionAmt;
    private String symbol;
    private String unRealizedProfit;
    private String positionSide;
}
