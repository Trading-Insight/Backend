package com.tradin.module.strategy.service.dto;

import com.tradin.module.history.domain.repository.dao.HistoryDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class HistoryCache {
    private final List<HistoryDao> histories;

    public static HistoryCache of(List<HistoryDao> histories) {
        return new HistoryCache(histories);
    }
}
