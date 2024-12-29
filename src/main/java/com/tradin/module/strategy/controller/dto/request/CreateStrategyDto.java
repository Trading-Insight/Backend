package com.tradin.module.strategy.controller.dto.request;

import com.tradin.module.strategy.domain.CoinType;
import com.tradin.module.strategy.domain.Position;
import com.tradin.module.strategy.domain.StrategyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateStrategyDto {
    private final String name;
    private final StrategyType strategyType;
    private final CoinType coinType;
    private final double profitFactor;
    private final double winningRate;
    private final double simpleProfitRate;
    private final double compoundProfitRate;
    private final double totalProfitRate;
    private final double totalLossRate;
    private final double totalTradeCount;
    private final int winCount;
    private final int lossCount;
    private final Position currentPosition;
    private final int averageHoldingPeriod;
    private final double averageProfitRate;
}
