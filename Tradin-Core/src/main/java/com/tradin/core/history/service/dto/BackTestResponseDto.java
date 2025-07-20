package com.tradin.core.history.service.dto;


import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.strategy.domain.repository.dao.StrategyInfoDao;
import java.util.List;

public record BackTestResponseDto(StrategyInfoDao strategyInfoDao, List<HistoryDao> historyDaos) {

    public static BackTestResponseDto of(StrategyInfoDao strategyInfoDao, List<HistoryDao> historyDaos) {
        return new BackTestResponseDto(strategyInfoDao, historyDaos);
    }
}
