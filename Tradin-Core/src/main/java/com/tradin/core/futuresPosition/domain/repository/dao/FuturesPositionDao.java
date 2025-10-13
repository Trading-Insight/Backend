package com.tradin.core.futuresPosition.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresOrder.domain.vo.Margin;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;

public record FuturesPositionDao(
    Long id,
    CoinType coinType,
    TradingType tradingType,
    Price entryPrice,
    Price liquidationPrice,
    Amount amount,
    Integer leverage,
    Margin margin
) {
    @QueryProjection
    public FuturesPositionDao(Long id, CoinType coinType, TradingType tradingType, Price entryPrice, Price liquidationPrice, Amount amount, Integer leverage, Margin margin) {
        this.id = id;
        this.coinType = coinType;
        this.tradingType = tradingType;
        this.entryPrice = entryPrice;
        this.liquidationPrice = liquidationPrice;
        this.amount = amount;
        this.leverage = leverage;
        this.margin = margin;
    }
}
