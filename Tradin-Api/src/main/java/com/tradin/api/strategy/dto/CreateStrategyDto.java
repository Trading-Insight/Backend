package com.tradin.api.strategy.dto;

import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.StrategyType;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.strategy.domain.vo.WinRate;
import lombok.Getter;

@Getter
public class CreateStrategyDto {
    private final String name;
    private final StrategyType strategyType;
    private final CoinType coinType;
    private final ProfitFactor profitFactor;
    private final WinRate winningRate;
    private final ProfitRate simpleProfitRate;
    private final ProfitRate compoundProfitRate;
    private final ProfitRate totalProfitRate;
    private final ProfitRate totalLossRate;
    private final int totalTradeCount;
    private final int winCount;
    private final int lossCount;
    private final Position currentPosition;
    private final int averageHoldingPeriod;
    private final ProfitRate averageProfitRate;

    public CreateStrategyDto(String name, StrategyType strategyType, CoinType coinType, ProfitFactor profitFactor, WinRate winningRate, ProfitRate simpleProfitRate, ProfitRate compoundProfitRate, ProfitRate totalProfitRate, ProfitRate totalLossRate, int totalTradeCount, int winCount, int lossCount, Position currentPosition, int averageHoldingPeriod, ProfitRate averageProfitRate) {
        this.name = name;
        this.strategyType = strategyType;
        this.coinType = coinType;
        this.profitFactor = profitFactor;
        this.winningRate = winningRate;
        this.simpleProfitRate = simpleProfitRate;
        this.compoundProfitRate = compoundProfitRate;
        this.totalProfitRate = totalProfitRate;
        this.totalLossRate = totalLossRate;
        this.totalTradeCount = totalTradeCount;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.currentPosition = currentPosition;
        this.averageHoldingPeriod = averageHoldingPeriod;
        this.averageProfitRate = averageProfitRate;
    }
}
