package com.tradin.core.strategy.service.dto;

import com.tradin.core.history.domain.repository.dao.HistoryDao;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryCache {

    private final List<HistoryDao> histories;

    public static HistoryCache of(List<HistoryDao> histories) {
        return new HistoryCache(histories);
    }
}
