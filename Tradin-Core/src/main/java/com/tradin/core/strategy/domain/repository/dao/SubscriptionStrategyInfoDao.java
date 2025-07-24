package com.tradin.core.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.strategy.domain.vo.WinRate;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SubscriptionStrategyInfoDao {
    private final long id;
    private final String name;

    @Schema(description = "코인명", example = "BITCOIN")
    private final CoinType coinType;

    @Schema(description = "수익팩터")
    private final ProfitFactor profitFactor;

    @Schema(description = "승률")
    private final WinRate winningRate;

    @Schema(description = "복리 기준 수익률 -> 누적 손익률")
    private final ProfitRate compoundProfitRate;

    @Schema(description = "평균 수익률")
    private final ProfitRate averageProfitRate;

    @QueryProjection
    public SubscriptionStrategyInfoDao(long id, String name, CoinType coinType, ProfitFactor profitFactor, WinRate winningRate, ProfitRate compoundProfitRate, ProfitRate averageProfitRate) {
        this.id = id;
        this.name = name;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.compoundProfitRate = compoundProfitRate;
        this.averageProfitRate = averageProfitRate;
    }
}
