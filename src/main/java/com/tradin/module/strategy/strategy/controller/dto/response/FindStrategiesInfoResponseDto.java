package com.tradin.module.strategy.strategy.controller.dto.response;

import com.tradin.module.strategy.strategy.domain.repository.dao.StrategyInfoDao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


public record FindStrategiesInfoResponseDto (
    @Schema(description = "전략 리스트") List<StrategyInfoDao> strategiesInfos
) {
    public static FindStrategiesInfoResponseDto of(List<StrategyInfoDao> strategiesInfos) {
        return new FindStrategiesInfoResponseDto(strategiesInfos);
    }
}