package com.tradin.core.strategy.implement;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.repository.StrategyRepository;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyReader {

    private final StrategyRepository strategyRepository;

    public List<StrategyInfoDao> findFutureStrategyInfoDaos() {
        return strategyRepository.findAllFutureStrategiesInfoDao();
    }

    public List<StrategyInfoDao> findSpotStrategyInfoDaos() {
        return strategyRepository.findAllSpotStrategiesInfoDao();
    }

    public StrategyInfoDao findStrategyInfoDaoById(Long id) {
        return strategyRepository.findStrategyInfoDaoById(id)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    public Strategy findStrategyById(Long id) {
        return strategyRepository.findById(id)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    public List<Strategy> findStrategiesByIds(List<Long> ids) {
        return strategyRepository.findAllByIds(ids);
    }
}
