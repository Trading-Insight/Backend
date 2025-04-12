package com.tradin.module.subscription.domain.repository.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.tradin.module.strategy.domain.Type;
import com.tradin.module.subscription.domain.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "전략 구독 목록")
public record SubscriptionsDao(
    @Schema(description = "전략 구독 ID", example = "1") Long id,
    @Schema(description = "계좌 ID", example = "1") Long accountId,
    @Schema(description = "전략 ID", example = "1") Long strategyId,
    @Schema(description = "전략 이름", example = "전략 이름") String strategyName,
    @Schema(description = "전략 타입") Type strategyType,
    @Schema(description = "구독 시작일", example = "2025-01-01") LocalDateTime startDate,
    @Schema(description = "구독 종료일", example = "2025-12-31") LocalDateTime endDate,
    @Schema(description = "구독 상태", example = "ACTIVE") SubscriptionStatus status) {

    @JsonCreator
    @QueryProjection
    public SubscriptionsDao(Long id, Long accountId, Long strategyId, String strategyName,
        @JsonProperty("strategyType") Type strategyType, LocalDateTime startDate, LocalDateTime endDate,
        SubscriptionStatus status) {
        this.id = id;
        this.accountId = accountId;
        this.strategyId = strategyId;
        this.strategyName = strategyName;
        this.strategyType = strategyType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
