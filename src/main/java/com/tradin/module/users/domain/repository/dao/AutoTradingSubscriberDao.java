package com.tradin.module.users.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class AutoTradingSubscriberDao {
    private final int leverage;
    private final int quantityRate;
    private final String binanceApiKey;
    private final String binanceSecretKey;

    @QueryProjection
    public AutoTradingSubscriberDao(int leverage, int quantityRate, String binanceApiKey, String binanceSecretKey) {
        this.leverage = leverage;
        this.quantityRate = quantityRate;
        this.binanceApiKey = binanceApiKey;
        this.binanceSecretKey = binanceSecretKey;
    }
}
