package com.tradin.core.history.service;

import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.history.service.dto.BackTestDto;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import com.tradin.core.strategy.service.StrategyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryFacadeService {
    private final HistoryService historyService;
    private final StrategyService strategyService;

    @Transactional
    public void createTestHistory() {
        Strategy strategy = strategyService.findStrategyById(1L);
        historyService.createTestHistory(strategy);
    }

    @Transactional
    public BackTestResponseDto backTest(BackTestDto request, Pageable pageable) {
        strategyService.validateExistStrategy(request.getId());
        StrategyInfoDao strategyInfoDao = strategyService.findStrategyInfoById(request.getId());
        List<HistoryDao> historyDaos = historyService.backTest(request, pageable);

        return BackTestResponseDto.of(strategyInfoDao, historyDaos);
    }
}
