package com.tradin.module.strategy.strategy.controller.dto.response;

import com.tradin.module.strategy.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FindSubscriptionStrategiesInfoResponseDto {
    private final List<SubscriptionStrategyInfoDao> strategiesInfo;

}
