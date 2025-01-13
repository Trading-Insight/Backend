package com.tradin.module.strategy.implement;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.domain.repository.StrategyRepository;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyReader {

    private final StrategyRepository strategyRepository;

    public List<StrategyInfoDao> readFutureStrategyInfoDaos() {
        return strategyRepository.findAllFutureStrategiesInfoDao();
    }

    public List<StrategyInfoDao> readSpotStrategyInfoDaos() {
        return strategyRepository.findAllSpotStrategiesInfoDao();
    }

    public StrategyInfoDao readStrategyInfoDaoById(Long id) {
        return findStrategyInfoDaoById(id);
    }

    private StrategyInfoDao findStrategyInfoDaoById(Long id) {
        return strategyRepository.findStrategyInfoDaoById(id)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
    }

//    public List<SubscriptionStrategyInfoDao> findSubscriptionStrategyInfoDaos() {
//        return strategyRepository.findSubscriptionStrategiesInfoDao()
//            .orElse(Collections.emptyList());
//    }
//
//    public Strategy findByName(String name) {
//        return strategyRepository.findByName(name)
//            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
//    }
//
//    public Strategy findById(Long id) {
//        return strategyRepository.findById(id)
//            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
//    }
}
