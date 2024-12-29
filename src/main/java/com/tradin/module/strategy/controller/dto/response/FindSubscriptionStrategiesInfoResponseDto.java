package com.tradin.module.strategy.controller.dto.response;

import com.tradin.module.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FindSubscriptionStrategiesInfoResponseDto {
    private final List<SubscriptionStrategyInfoDao> strategiesInfo;

}
