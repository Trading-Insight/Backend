package com.tradin.module.futures.order.feign.client.dto.binance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeLeverageDto {

    private int leverage;
    private String maxNotionalValue;
    private String symbol;
}
