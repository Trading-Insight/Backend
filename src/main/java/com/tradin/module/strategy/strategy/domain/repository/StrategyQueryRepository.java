package com.tradin.module.strategy.strategy.domain.repository;

import com.tradin.module.strategy.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import java.util.List;
import java.util.Optional;

public interface StrategyQueryRepository {

    List<StrategyInfoDao> findAllFutureStrategiesInfoDao();

    Optional<List<SubscriptionStrategyInfoDao>> findSubscriptionStrategiesInfoDao();

    List<StrategyInfoDao> findAllSpotStrategiesInfoDao();

    Optional<StrategyInfoDao> findStrategyInfoDaoById(Long id);
}
