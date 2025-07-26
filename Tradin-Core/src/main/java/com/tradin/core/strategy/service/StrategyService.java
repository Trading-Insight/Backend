package com.tradin.core.strategy.service;

import static com.tradin.core.common.exception.ExceptionType.SAME_POSITION_REQUEST_EXCEPTION;
import static com.tradin.core.strategy.domain.TimeFrameType.ONE_HOUR;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Count;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Rate;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.StrategyType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.domain.Type;
import com.tradin.core.strategy.domain.repository.StrategyRepository;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.core.strategy.domain.vo.ProfitFactor;
import com.tradin.core.strategy.domain.vo.ProfitRate;
import com.tradin.core.strategy.domain.vo.WinRate;
import com.tradin.core.price.domain.vo.Price;
import com.tradin.core.strategy.service.dto.FindStrategiesInfoResponseDto;
import com.tradin.core.strategy.service.dto.WebHookDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;

    public void createStrategy() {
        Strategy strategy = Strategy.of(
            "test",
            Type.of(StrategyType.FUTURE, CoinType.BTC, ONE_HOUR),
            Rate.of(
                WinRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO),
                ProfitRate.of(BigDecimal.ZERO)
            ),
            Count.of(
                0,
                0,
                0
            ),
            Position.of(
                TradingType.NONE,
                LocalDateTime.now(),
                Price.of(BigDecimal.ZERO)
            ),
            ProfitFactor.of(BigDecimal.ZERO),
            0
        );
        strategyRepository.save(strategy);
    }

    public FindStrategiesInfoResponseDto findFutureStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyRepository.findAllFutureStrategiesInfoDao();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

    public FindStrategiesInfoResponseDto findSpotStrategiesInfo() {
        List<StrategyInfoDao> strategiesInfo = strategyRepository.findAllSpotStrategiesInfoDao();
        return FindStrategiesInfoResponseDto.of(strategiesInfo);
    }

    public void handleFutureWebHook(WebHookDto request) {
        Strategy strategy = findStrategyById(request.getId());
        Position position = request.getPosition();

        validateSamePosition(strategy, position);
        updateStrategy(strategy, position);
    }

    public Strategy findStrategyById(Long id) {
        return strategyRepository.findById(id)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    public List<Strategy> findStrategiesByIds(List<Long> ids) {
        return strategyRepository.findAllByIds(ids);
    }

    public void validateExistStrategy(Long id) {
        findStrategyById(id);
    }

    private void validateSamePosition(Strategy strategy, Position position) {
        if (strategy.getCurrentPosition().getTradingType() == position.getTradingType()) {
            throw new TradinException(SAME_POSITION_REQUEST_EXCEPTION);
        }
    }

    private void updateStrategy(Strategy strategy, Position strategyPosition) {
        strategy.updateRateAndCount(strategyPosition.getPrice(), strategyPosition.getTime());
        strategy.updateCurrentPosition(strategyPosition);
    }
}
