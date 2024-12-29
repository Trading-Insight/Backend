package com.tradin.module.history.domain.repository;

import com.tradin.module.history.domain.History;
import com.tradin.module.history.domain.repository.dao.HistoryDao;

import java.util.List;
import java.util.Optional;

public interface HistoryQueryRepository {
    Optional<History> findLastHistoryByStrategyId(Long id);

    List<HistoryDao> findHistoryDaoByStrategyId(Long id);
}
