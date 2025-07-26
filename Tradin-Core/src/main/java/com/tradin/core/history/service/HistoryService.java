package com.tradin.core.history.service;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_HISTORY_EXCEPTION;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.history.domain.History;
import com.tradin.core.history.domain.repository.HistoryRepository;
import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.history.service.dto.BackTestDto;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tradin.core.price.domain.vo.Price;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public void createHistory(Strategy strategy) {
        Position position = Position.of(TradingType.SHORT, LocalDateTime.now(), Price.of(new BigDecimal(90000)));
        createHistory(strategy, position);
    }

    public void createHistory(Strategy strategy, Position position) {
        History newHistory = History.of(position, strategy);
        historyRepository.save(newHistory);
    }

    public void closeOpenHistory(Long strategyId, Position position) {
        History history = findOpenHistoryByStrategyId(strategyId);
        closeHistory(history, position);
    }

    public BackTestResponseDto backTest(BackTestDto request, Pageable pageable) {
        List<HistoryDao> historyDaos = findHistoryByIdAndPeriodAndTradingType(request, pageable);
        return BackTestResponseDto.of(null, historyDaos); // StrategyInfoDao will be provided by FacadeService
    }

    private void closeHistory(History history, Position position) {
        history.closeHistory(position);
    }

    private List<HistoryDao> findHistoryByIdAndPeriodAndTradingType(BackTestDto request, Pageable pageable) {
        List<HistoryDao> histories = historyRepository.findHistoryByStrategyId(request.getId());
        
        return histories.stream()
            .filter(history -> isInPeriod(history, request.getStartDate(), request.getEndDate()))
            .filter(history -> isCorrespondTradingType(history, request.getTradingType()))
            .toList();
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

    private History findOpenHistoryByStrategyId(Long strategyId) {
        return historyRepository.findOpenHistoryByStrategyId(strategyId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_HISTORY_EXCEPTION));
    }
}
