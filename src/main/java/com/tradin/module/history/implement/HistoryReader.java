package com.tradin.module.history.implement;

import com.tradin.module.history.domain.repository.HistoryRepository;
import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.strategy.domain.TradingType;
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

        List<HistoryDao> cachedHistories = HistoryCacheReader.readHistoryByIdAndPeriod(cacheKey, startDate, endDate, pageable);

        if (isCachedHistoriesEmpty(cachedHistories)) {
            return findHistoryByIdAndPeriodAndTradingType(strategyId, startDate, endDate, tradingType, cacheKey);
        }

        return filterByPeriodAndTradingType(cachedHistories, startDate, endDate, tradingType);
    }

    private List<HistoryDao> findHistoryByIdAndPeriodAndTradingType(Long strategyId, LocalDate startDate, LocalDate endDate, TradingType tradingType, String cacheKey) {
        List<HistoryDao> historyDaos = historyRepository.findHistoryDaoByStrategyId(strategyId);
        addHistoryCache(cacheKey, historyDaos);

        return filterByPeriodAndTradingType(historyDaos, startDate, endDate, tradingType);
    }

    private boolean isCachedHistoriesEmpty(List<HistoryDao> historyDaos) {
        return historyDaos.isEmpty();
    }

    private void addHistoryCache(String cacheKey, List<HistoryDao> historyDaos) {
        HistoryCacheProcessor.addHistoryCache(cacheKey, historyDaos);
    }

    private List<HistoryDao> filterByPeriodAndTradingType(List<HistoryDao> historyDaos, LocalDate startDate, LocalDate endDate, TradingType tradingType) {
        List<HistoryDao> filteredHistoryDaos = historyDaos.stream()
            .filter(historyDao -> isInPeriod(historyDao, startDate, endDate))
            .filter(historyDao -> isCorrespondTradingType(historyDao, tradingType))
            .toList();

        return calculateCompoundProfitRate(filteredHistoryDaos, startDate, endDate);
    }

    private boolean isInPeriod(HistoryDao historyDao, LocalDate startDate, LocalDate endDate) {
        return historyDao.entryPosition().getTime().toLocalDate().isAfter(startDate) &&
            historyDao.exitPosition().getTime().toLocalDate().isBefore(endDate);
    }

    private boolean isCorrespondTradingType(HistoryDao historyDao, TradingType tradingType) {
        return switch (tradingType) {
            case LONG -> historyDao.entryPosition().getTradingType().equals(TradingType.LONG);
            case SHORT -> historyDao.entryPosition().getTradingType().equals(TradingType.SHORT);
            case BOTH -> historyDao.entryPosition().getTradingType().equals(TradingType.LONG) ||
                historyDao.entryPosition().getTradingType().equals(TradingType.SHORT);
            default -> false;
        };
    }

    private List<HistoryDao> calculateCompoundProfitRate(List<HistoryDao> filteredHistoryDaos, LocalDate startDate, LocalDate endDate) {
        double[] cumulativeProfitRate = {0.0};

        return filteredHistoryDaos.stream()
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
