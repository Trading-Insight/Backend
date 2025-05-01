package com.tradin.module.strategy.history.domain.repository.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.strategy.domain.Position;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "히스토리 정보")
public record HistoryDao(
    @Schema(description = "히스토리 ID", example = "1") Long id,
    @Schema(description = "진입 포지션") Position entryPosition,
    @Schema(description = "종료 포지션") Position exitPosition,
    @Schema(description = "수익률") double profitRate,
    @Schema(description = "복리 수익률") double compoundProfitRate) {

    @JsonCreator
    @QueryProjection
    public HistoryDao(@JsonProperty("id") Long id,
        @JsonProperty("entryPosition") Position entryPosition,
        @JsonProperty("exitPosition") Position exitPosition,
        @JsonProperty("profitRate") double profitRate) {
        this(id, entryPosition, exitPosition, profitRate, 0);
    }
}