package com.tradin.core.balance.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.strategy.domain.CoinType;

public record BalanceDaos(
    Long id,
    CoinType coinType,
    Amount amount
) {
    @QueryProjection
    public BalanceDaos(Long id, CoinType coinType, Amount amount) {
        this.id = id;
        this.coinType = coinType;
        this.amount = amount;
    }

}
