package com.tradin.module.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.domain.CoinType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SubscriptionStrategyInfoDao {
    private final long id;
    private final String name;

    @Schema(description = "코인명", example = "BITCOIN")
    private final CoinType coinType;

    @Schema(description = "수익팩터")
    private final double profitFactor;

    @Schema(description = "승률")
    private final double winningRate;

    @Schema(description = "복리 기준 수익률 -> 누적 손익률")
    private final double compoundProfitRate;

    @Schema(description = "평균 수익률")
    private final double averageProfitRate;

    @QueryProjection
    public SubscriptionStrategyInfoDao(long id, String name, CoinType coinType, double profitFactor, double winningRate, double compoundProfitRate, double averageProfitRate) {
        this.id = id;
        this.name = name;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.compoundProfitRate = compoundProfitRate;
        this.averageProfitRate = averageProfitRate;
    }
}
