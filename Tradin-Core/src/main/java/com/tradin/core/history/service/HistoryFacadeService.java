package com.tradin.core.history.service;

import com.tradin.core.history.service.dto.BackTestDto;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.service.StrategyService;
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
    public void createHistory(Long strategyId) {
        Strategy strategy = strategyService.findStrategyById(strategyId);
        historyService.createHistory(strategy);
    }

    @Transactional(readOnly = true)
    public BackTestResponseDto backTest(BackTestDto request, Pageable pageable) {
        strategyService.validateExistStrategy(request.getId());
        return historyService.backTest(request, pageable);
    }
}
