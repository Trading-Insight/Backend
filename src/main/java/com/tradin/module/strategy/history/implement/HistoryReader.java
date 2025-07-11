package com.tradin.module.strategy.history.implement;

import static com.tradin.common.exception.ExceptionType.NOT_FOUND_HISTORY_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.strategy.history.domain.History;
import com.tradin.module.strategy.history.domain.repository.HistoryRepository;
import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import com.tradin.module.strategy.strategy.domain.TradingType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryReader {

    private final HistoryCacheReader HistoryCacheReader;
    private final HistoryCacheProcessor HistoryCacheProcessor;
    private final HistoryRepository historyRepository;

    public List<HistoryDao> readHistoryByIdAndPeriodAndTradingType(Long strategyId, LocalDate startDate, LocalDate endDate, TradingType tradingType, Pageable pageable) {
        String cacheKey = "strategyId:" + strategyId;

        List<HistoryDao> cachedHistories = readCachedHistories(startDate, endDate, pageable, cacheKey);

        if (isCachedHistoriesEmpty(cachedHistories)) {
            return findAndFilterHistoriesByIdAndPeriodAndTradingType(strategyId, startDate, endDate, tradingType, cacheKey);
        }

        return filterByPeriodAndTradingType(cachedHistories, startDate, endDate, tradingType);
    }

    public History findOpenHistoryByStrategyId(Long strategyId) {
        return historyRepository.findOpenHistoryByStrategyId(strategyId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_HISTORY_EXCEPTION));
    }

    private List<HistoryDao> readCachedHistories(LocalDate startDate, LocalDate endDate, Pageable pageable, String cacheKey) {
        return HistoryCacheReader.readHistoryByIdAndPeriod(cacheKey, startDate, endDate, pageable);
    }

    private List<HistoryDao> findAndFilterHistoriesByIdAndPeriodAndTradingType(Long strategyId, LocalDate startDate, LocalDate endDate, TradingType tradingType, String cacheKey) {
        List<HistoryDao> histories = historyRepository.findHistoryByStrategyId(strategyId);
        addHistoryCache(cacheKey, histories);

        return filterByPeriodAndTradingType(histories, startDate, endDate, tradingType);
    }

    private boolean isCachedHistoriesEmpty(List<HistoryDao> histories) {
        return histories.isEmpty();
    }

    private void addHistoryCache(String cacheKey, List<HistoryDao> histories) {
        HistoryCacheProcessor.addHistoryCache(cacheKey, histories);
    }

    private List<HistoryDao> filterByPeriodAndTradingType(List<HistoryDao> histories, LocalDate startDate, LocalDate endDate, TradingType tradingType) {
        List<HistoryDao> filteredHistories = histories.stream()
            .filter(history -> isInPeriod(history, startDate, endDate))
            .filter(history -> isCorrespondTradingType(history, tradingType))
            .toList();

        return calculateCompoundProfitRate(filteredHistories, startDate, endDate);
    }

    private boolean isInPeriod(HistoryDao history, LocalDate startDate, LocalDate endDate) {
        return history.entryPosition().getTime().toLocalDate().isAfter(startDate) &&
            history.exitPosition().getTime().toLocalDate().isBefore(endDate);
    }

    private boolean isCorrespondTradingType(HistoryDao history, TradingType tradingType) {
        return switch (tradingType) {
            case LONG -> history.entryPosition().getTradingType().equals(TradingType.LONG);
            case SHORT -> history.entryPosition().getTradingType().equals(TradingType.SHORT);
            case BOTH -> history.entryPosition().getTradingType().equals(TradingType.LONG) ||
                history.entryPosition().getTradingType().equals(TradingType.SHORT);
            default -> false;
        };
    }

    private List<HistoryDao> calculateCompoundProfitRate(List<HistoryDao> filteredHistories, LocalDate startDate, LocalDate endDate) {
        double[] cumulativeProfitRate = {0.0};

        return filteredHistories.stream()
            .map(history -> {
                cumulativeProfitRate[0] = calculateCompound(cumulativeProfitRate[0], history.profitRate());
                return new HistoryDao(
                    history.id(),
                    history.entryPosition(),
                    history.exitPosition(),
                    history.profitRate(),
                    cumulativeProfitRate[0]
                );
            })
            .collect(Collectors.toList());
    }

    private double calculateCompound(double cumulativeProfitRate, double newProfitRate) {
        return (1 + cumulativeProfitRate / 100) * (1 + newProfitRate / 100) - 1;
    }
}
