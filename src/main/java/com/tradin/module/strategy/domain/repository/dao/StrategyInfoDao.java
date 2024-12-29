package com.tradin.module.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.domain.CoinType;
import com.tradin.module.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StrategyInfoDao {
    private final Long id;
    private final String name;

    @Schema(description = "코인명", example = "BITCOIN")
    private final CoinType coinType;

    @Schema(description = "수익팩터")
    private final double profitFactor;

    @Schema(description = "승률")
    private final double winningRate;

    @Schema(description = "단리 기준 수익률")
    private final double simpleProfitRate;

    @Schema(description = "복리 기준 수익률 -> 누적 손익률")
    private final double compoundProfitRate;

    @Schema(description = "총 수익률(수익율+손실율 아님)")
    private final double totalProfitRate;

    @Schema(description = "총 손실률")
    private final double totalLossRate;

    @Schema(description = "평균 수익률")
    private final double averageProfitRate;

    @Schema(description = "총 거래 횟수")
    private final int totalTradeCount;

    @Schema(description = "승리 횟수")
    private final int winCount;

    @Schema(description = "패배 횟수")
    private final int lossCount;

    @Schema(description = "거래 타입", example = "LONG")
    private final TradingType tradingType;

    @Schema(description = "진입 시간")
    private final LocalDateTime time;

    @Schema(description = "진입 가격")
    private final int price;

    @Schema(description = "평균 봉 수")
    private final int averageHoldingPeriod;

    @QueryProjection
    public StrategyInfoDao(Long id, String name, CoinType coinType, double profitFactor, double winningRate, double simpleProfitRate, double compoundProfitRate, double totalProfitRate, double totalLossRate, double averageProfitRate, int totalTradeCount, int winCount, int lossCount, TradingType tradingType, LocalDateTime time, int price, int averageHoldingPeriod) {
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
