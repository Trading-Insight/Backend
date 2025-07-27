package com.tradin.core.history.domain.repository.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Schema(description = "히스토리 정보")
public record HistoryDao(
    Long id,
    Position entryPosition,
    Position exitPosition,
    ProfitRate profitRate,
    ProfitRate compoundProfitRate) {

    @JsonCreator
    @QueryProjection
    public HistoryDao(@JsonProperty("id") Long id,
        @JsonProperty("entryPosition") Position entryPosition,
        @JsonProperty("exitPosition") Position exitPosition,
        @JsonProperty("profitRate") ProfitRate profitRate) {
        this(id, entryPosition, exitPosition, profitRate == null ? null : ProfitRate.of(profitRate.getValue() == null ? null : profitRate.getValue().setScale(2, profitRate.getValue().signum() >= 0 ? RoundingMode.DOWN : RoundingMode.UP)), ProfitRate.of(null));
    }
}