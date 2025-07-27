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
import java.math.RoundingMode;
import java.time.Duration;
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

    public Strategy updateStrategyStatistics(WebHookDto request) {
        Strategy strategy = findStrategyById(request.getId());
        Position position = request.getPosition();

        validateIsSamePosition(strategy, position);
        updateStatistics(strategy, position);

        return strategy;
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

    public StrategyInfoDao findStrategyInfoById(Long id) {
        return strategyRepository.findStrategyInfoDaoById(id)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_STRATEGY_EXCEPTION));
    }

    public void validateIsSamePosition(Strategy strategy, Position position) {
        if (strategy.getCurrentPosition().getTradingType() == position.getTradingType()) {
            throw new TradinException(SAME_POSITION_REQUEST_EXCEPTION);
        }
    }

    private void updateStatistics(Strategy strategy, Position newPosition) {
        // 수익률 계산
        ProfitRate profitRate = calculateProfitRate(strategy, newPosition);
        
        // 승패 판정
        boolean isWin = isWin(profitRate);
        
        // 기본 통계 업데이트
        if (isWin) {
            strategy.increaseWinCount();
            strategy.updateTotalProfitRate(profitRate);
        } else {
            strategy.increaseLossCount();
            strategy.updateTotalLossRate(profitRate);
        }
        
        // 평균 보유 기간 업데이트
        updateAverageHoldingPeriod(strategy, newPosition);
        
        // 총 거래 횟수 증가
        strategy.increaseTotalTradeCount();
        
        // 수익 팩터 업데이트
        updateProfitFactor(strategy);
        
        // 기타 통계 업데이트
        strategy.updateWinRate();
        strategy.updateSimpleProfitRate();
        strategy.updateCompoundProfitRate(profitRate);
        strategy.updateAverageProfitRate();
        
        // 최종 포지션 업데이트
        strategy.updateCurrentPosition(newPosition);
    }

    private ProfitRate calculateProfitRate(Strategy strategy, Position newPosition) {
        Price currentPrice = strategy.getCurrentPosition().getPrice();
        Price newPrice = newPosition.getPrice();
        
        if (strategy.isCurrentPositionLong()) {
            // 롱 포지션: (새로운 가격 - 진입 가격) / 진입 가격 * 100
            return ProfitRate.of(
                newPrice.getValue().subtract(currentPrice.getValue())
                    .divide(currentPrice.getValue(), 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
            );
        } else {
            // 숏 포지션: (진입 가격 - 새로운 가격) / 진입 가격 * 100
            return ProfitRate.of(
                currentPrice.getValue().subtract(newPrice.getValue())
                    .divide(currentPrice.getValue(), 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
            );
        }
    }

    private boolean isWin(ProfitRate profitRate) {
        return profitRate.getValue().compareTo(BigDecimal.ZERO) >= 0;
    }

    private void updateAverageHoldingPeriod(Strategy strategy, Position newPosition) {
        LocalDateTime entryTime = strategy.getCurrentPosition().getTime();
        LocalDateTime exitTime = newPosition.getTime();
        
        long holdingPeriodMinutes = Duration.between(entryTime, exitTime).toMinutes();
        int timeFrameValue = strategy.getType().getTimeFrameType().getValue();
        
        // 평균 보유 기간 계산: (현재 보유 기간 + 기존 평균 * 기존 거래 횟수) / (기존 거래 횟수 + 1)
        int currentHoldingPeriod = (int) (holdingPeriodMinutes / timeFrameValue);
        int totalTradeCount = strategy.getCount().getTotalTradeCount();
        int currentAverage = strategy.getAverageHoldingPeriod();
        
        int newAverage = (currentHoldingPeriod + (currentAverage * totalTradeCount)) / (totalTradeCount + 1);
        
        strategy.updateAverageHoldingPeriod(newAverage);
    }

    private void updateProfitFactor(Strategy strategy) {
        ProfitRate totalProfitRate = strategy.getRate().getTotalProfitRate();
        ProfitRate totalLossRate = strategy.getRate().getTotalLossRate();
        
        if (totalLossRate == null || totalLossRate.getValue ( ) == null || totalLossRate.getValue ( ) .compareTo ( BigDecimal.ZERO ) == 0) {
            strategy.updateProfitFactor(ProfitFactor.of(BigDecimal.ZERO));
        } else {
            BigDecimal profitFactorValue = totalProfitRate.getValue()
                .divide(totalLossRate.getValue(), 2, RoundingMode.DOWN);
            strategy.updateProfitFactor(ProfitFactor.of(profitFactorValue));
        }
    }
}
