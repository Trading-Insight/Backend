package com.tradin.module.strategy.controller.dto.response;

import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FindStrategiesInfoResponseDto {
    private final List<StrategyInfoDao> strategiesInfo;
}
