package com.tradin.module.feign.client.dto.binance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FutureAccountBalanceDto {
    private String accountAlias;
    private String asset;
    private String balance;
    private String crossWalletBalance;
    private String crossUnPnl;
    private String availableBalance;
    private String maxWithdrawAmount;
}
