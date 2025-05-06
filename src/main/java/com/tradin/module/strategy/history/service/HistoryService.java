package com.tradin.module.strategy.history.service;

import com.tradin.module.strategy.history.controller.dto.response.BackTestResponseDto;
import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import com.tradin.module.strategy.history.implement.HistoryProcessor;
import com.tradin.module.strategy.history.implement.HistoryReader;
import com.tradin.module.strategy.history.service.dto.BackTestDto;
import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.domain.Strategy;
import com.tradin.module.strategy.strategy.domain.TradingType;
import com.tradin.module.strategy.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.module.strategy.strategy.implement.StrategyReader;
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
