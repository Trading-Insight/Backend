package com.tradin.core.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;

import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.strategy.domain.vo.WinRate;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.price.domain.vo.Price;

@Schema(description = "전략 정보")
public record StrategyInfoDao(
    Long id,
    String name,
    CoinType coinType,
    ProfitFactor profitFactor,
    WinRate winningRate,
    ProfitRate simpleProfitRate,
    ProfitRate compoundProfitRate,
    ProfitRate totalProfitRate,
    ProfitRate totalLossRate,
    ProfitRate averageProfitRate,
    int totalTradeCount,
    int winCount,
    int lossCount,
    TradingType tradingType,
    LocalDateTime time,
    Price price,
    int averageHoldingPeriod) {

    @QueryProjection
    public StrategyInfoDao(Long id, String name, CoinType coinType, ProfitFactor profitFactor,
        WinRate winningRate, ProfitRate simpleProfitRate, ProfitRate compoundProfitRate,
        ProfitRate totalProfitRate, ProfitRate totalLossRate, ProfitRate averageProfitRate, int totalTradeCount,
        int winCount, int lossCount, TradingType tradingType, LocalDateTime time, Price price,
        int averageHoldingPeriod) {
        this.id = id;
        this.name = name;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.simpleProfitRate = simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.totalLossRate = totalLossRate;
        this.averageProfitRate = averageProfitRate;
        this.totalTradeCount = totalTradeCount;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.tradingType = tradingType;
        this.time = time;
        this.price = price;
        this.averageHoldingPeriod = averageHoldingPeriod;
    }
}
