package com.tradin.core.history.service;


import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.history.implement.HistoryProcessor;
import com.tradin.core.history.implement.HistoryReader;
import com.tradin.core.history.service.dto.BackTestDto;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.core.strategy.implement.StrategyReader;
import java.time.LocalDateTime;
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
    private final HistoryProcessor historyProcessor;

    @Transactional
    public void createHistory(Long strategyId) {
        Strategy strategy = strategyReader.findStrategyById(strategyId);
        Position position = Position.of(TradingType.SHORT, LocalDateTime.now(), 90000);
        historyProcessor.createHistory(strategy, position);
    }

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
        return strategyReader.findStrategyInfoDaoById(request.getId());
    }
}
