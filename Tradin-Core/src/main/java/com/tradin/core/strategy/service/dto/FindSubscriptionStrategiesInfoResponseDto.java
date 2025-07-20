package com.tradin.core.strategy.service.dto;

import com.tradin.core.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindSubscriptionStrategiesInfoResponseDto {
    private final List<SubscriptionStrategyInfoDao> strategiesInfo;

}
