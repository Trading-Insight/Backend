package com.tradin.module.history.controller.dto.response;

import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.history.service.dto.StrategyInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BackTestResponseDto {
    private final StrategyInfoDto strategyInfoDto;
    private final List<HistoryDao> historyDaos;

    public static BackTestResponseDto of(StrategyInfoDto strategyInfoDto, List<HistoryDao> historyDaos) {
        return new BackTestResponseDto(strategyInfoDto, historyDaos);
    }
}
