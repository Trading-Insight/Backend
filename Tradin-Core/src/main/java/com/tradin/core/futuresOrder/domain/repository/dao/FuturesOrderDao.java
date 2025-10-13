package com.tradin.core.futuresOrder.domain.repository.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.querydsl.core.annotations.QueryProjection;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.futuresOrder.domain.OrderStatus;
import com.tradin.core.futuresOrder.domain.vo.Margin;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record FuturesOrderDao (
    Long id,
    TradingType tradingType,
    CoinType coinType,
    Price price,
    Amount amount,
    Margin margin,
    OrderStatus orderStatus,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {
    @JsonCreator
    @QueryProjection
    public FuturesOrderDao(Long id, TradingType tradingType, CoinType coinType, Price price, Amount amount, Margin margin, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.tradingType = tradingType;
        this.coinType = coinType;
        this.price = price;
        this.amount = amount;
        this.margin = margin;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}