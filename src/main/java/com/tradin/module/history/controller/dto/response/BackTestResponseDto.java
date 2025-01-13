package com.tradin.module.history.controller.dto.response;

import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.strategy.domain.repository.dao.StrategyInfoDao;
import java.util.List;

public record BackTestResponseDto(StrategyInfoDao strategyInfoDao, List<HistoryDao> historyDaos) {

    public static BackTestResponseDto of(StrategyInfoDao strategyInfoDao, List<HistoryDao> historyDaos) {
        return new BackTestResponseDto(strategyInfoDao, historyDaos);
    }
}
