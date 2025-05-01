package com.tradin.module.strategy.strategy.domain.repository.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.strategy.strategy.domain.TradingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "전략 정보")
public record StrategyInfoDao(
    @Schema(description = "전략Id", example = "1") Long id,
    @Schema(description = "전략명", example = "Strategy-1") String name,
    @Schema(description = "코인명", example = "BITCOIN") CoinType coinType,
    @Schema(description = "수익팩터") double profitFactor,
    @Schema(description = "승률") double winningRate,
    @Schema(description = "단리 기준 수익률") double simpleProfitRate,
    @Schema(description = "복리 기준 수익률 -> 누적 손익률") double compoundProfitRate,
    @Schema(description = "총 수익률(수익율+손실율 아님)") double totalProfitRate,
    @Schema(description = "총 손실률") double totalLossRate,
    @Schema(description = "평균 수익률") double averageProfitRate,
    @Schema(description = "총 거래 횟수") int totalTradeCount,
    @Schema(description = "승리 횟수") int winCount,
    @Schema(description = "패배 횟수") int lossCount,
    @Schema(description = "거래 타입", example = "LONG") TradingType tradingType,
    @Schema(description = "진입 시간") LocalDateTime time,
    @Schema(description = "진입 가격") int price,
    @Schema(description = "평균 봉 수") int averageHoldingPeriod) {

    @QueryProjection
    public StrategyInfoDao(Long id, String name, CoinType coinType, double profitFactor,
        double winningRate, double simpleProfitRate, double compoundProfitRate,
        double totalProfitRate, double totalLossRate, double averageProfitRate, int totalTradeCount,
        int winCount, int lossCount, TradingType tradingType, LocalDateTime time, int price,
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
