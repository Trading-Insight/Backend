package com.tradin.module.history.service;

import com.tradin.module.history.controller.dto.response.BackTestResponseDto;
import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.history.implement.HistoryReader;
import com.tradin.module.history.service.dto.BackTestDto;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.implement.StrategyReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {

    private final StrategyReader strategyReader;
    private final HistoryReader historyReader;

    @Transactional
    public BackTestResponseDto backTest(BackTestDto request, Pageable pageable) {
        StrategyInfoDao strategyInfoDao = readStrategyInfoDaoById(request);
        List<HistoryDao> historyDaos = readHistoryByIdAndPeriodAndTradingType(request, pageable);

        return BackTestResponseDto.of(strategyInfoDao, historyDaos);
    }

    private List<HistoryDao> readHistoryByIdAndPeriodAndTradingType(BackTestDto request, Pageable pageable) {
        return historyReader.readHistoryByIdAndPeriodAndTradingType(
            request.getId(),
            request.getStartDate(),
            request.getEndDate(),
            request.getTradingType(),
            pageable
        );
    }

    private StrategyInfoDao readStrategyInfoDaoById(BackTestDto request) {
        return strategyReader.readStrategyInfoDaoById(request.getId());
    }

//    private BackTestResponseDto calculateHistoryDaos(List<HistoryDao> historyDaos,
//        BackTestDto request) {
//        List<HistoryDao> histories = new ArrayList<>();
//        double compoundProfitRate = 0;
//        int winCount = 0;
//        int totalTradeCount = 0;
//        double simpleTotalProfitRate = 0;
//        double simpleWinProfitRate = 0;
//        double simpleLoseProfitRate = 0;
//
//        for (HistoryDao history : historyDaos) {
//            if (isInPeriod(history, request.getStartDate(), request.getEndDate()) &&
//                isCorrespondTradingType(history, request.getTradingType())) {
//
//                totalTradeCount++;
//                compoundProfitRate =
//                    (((1 + compoundProfitRate / 100.0) * (1 + history.getProfitRate() / 100.0)) - 1)
//                        * 100;
//                history.setCompoundProfitRate(compoundProfitRate);
//                histories.add(history);
//
//                double profitRate = history.getProfitRate();
//                if (profitRate > 0) {
//                    simpleWinProfitRate += profitRate;
//                    winCount++;
//                } else if (profitRate < 0) {
//                    simpleLoseProfitRate += Math.abs(profitRate);
//                }
//
//                simpleTotalProfitRate += profitRate;
//            }
//        }
//
//        Collections.reverse(histories);
//
//        double winRate = calculateWinRate(winCount, totalTradeCount);
//        double averageProfitRate = calculateAverageProfitRate(simpleTotalProfitRate,
//            totalTradeCount);
//        double profitFactor = calculateProfitFactor(simpleWinProfitRate, simpleLoseProfitRate);
//
//        StrategyInfoDto strategyInfoDto = StrategyInfoDto.of(request.getId(), request.getName(),
//            compoundProfitRate, winRate, profitFactor, totalTradeCount, averageProfitRate);
//
//        return BackTestResponseDto.of(strategyInfoDto, histories);
//    }
//
//
//    private double calculateWinRate(double winCount, int totalTradeCount) {
//        return totalTradeCount == 0 ? 0 : winCount / totalTradeCount * 100;
//    }
//
//    private double calculateAverageProfitRate(double simpleProfitRate, int totalTradeCount) {
//        return totalTradeCount == 0 ? 0 : simpleProfitRate / totalTradeCount;
//    }
//
//    private double calculateProfitFactor(double simpleWinProfitRate, double simpleLoseProfitRate) {
//        return simpleLoseProfitRate == 0 ? 0 : simpleWinProfitRate / simpleLoseProfitRate;
//    }
//
//
//    private boolean isInPeriod(HistoryDao history, LocalDate startDate, LocalDate endDate) {
//        return history.getEntryPosition().getTime().isAfter(startDate.atStartOfDay()) &&
//            history.getExitPosition().getTime().isBefore(endDate.atStartOfDay());
//    }
//
//    private boolean isCorrespondTradingType(HistoryDao historyDao, TradingType tradingType) {
//        return tradingType == historyDao.getEntryPosition().getTradingType() || tradingType == BOTH;
//    }
//
//    private static void calculateProfitRate(History history) {
//        history.calculateProfitRate();
//    }
//
//    private static void closeOpenPosition(History ongoingHistory, Position exitPosition) {
//        if (ongoingHistory.getExitPosition() != null) {
//            throw new TradinException(NOT_FOUND_OPEN_POSITION_EXCEPTION);
//
//        }
//        ongoingHistory.closeOpenPosition(exitPosition);
//    }
//
//    private History findLastHistoryByStrategyId(Long id) {
//        return historyRepository.findLastHistoryByStrategyId(id)
//            .orElseThrow(() -> new TradinException(NOT_FOUND_OPEN_POSITION_EXCEPTION));
//    }
//
//
//    public void closeOngoingHistory(Strategy strategy, Position exitPosition) {
//        History ongoingHistory = findLastHistoryByStrategyId(strategy.getId());
//        closeOpenPosition(ongoingHistory, exitPosition);
//        calculateProfitRate(ongoingHistory);
//    }
//
//    public void createNewHistory(Strategy strategy, Position position) {
//        History newHistory = History.of(position, strategy);
//        historyRepository.save(newHistory);
//    }
//
//    public void evictHistoryCache(Long strategyId) {
//        String cacheKey = "strategyId:" + strategyId;
//        historyRedisTemplate.delete(cacheKey);
//    }
}
