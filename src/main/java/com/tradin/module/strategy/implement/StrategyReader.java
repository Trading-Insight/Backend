package com.tradin.module.strategy.implement;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.domain.Strategy;
import com.tradin.module.strategy.domain.repository.StrategyRepository;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.domain.repository.dao.SubscriptionStrategyInfoDao;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyReader {
  private final StrategyRepository strategyRepository;

  public List<StrategyInfoDao> findFutureStrategyInfoDaos() {
    return strategyRepository.findFutureStrategiesInfoDao();
  }

  public List<StrategyInfoDao> findSpotStrategyInfoDaos() {
    return strategyRepository.findSpotStrategiesInfoDao();
  }

  public List<SubscriptionStrategyInfoDao> findSubscriptionStrategyInfoDaos() {
    return strategyRepository.findSubscriptionStrategiesInfoDao()
        .orElse(Collections.emptyList());
  }

  public Strategy findByName(String name) {
    return strategyRepository.findByName(name)
        .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
  }


  public Strategy findById(Long id) {
    return strategyRepository.findById(id)
        .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
  }
}
