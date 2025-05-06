package com.tradin.module.strategy.history.domain.repository;

import com.tradin.module.strategy.history.domain.History;
import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import java.util.List;
import java.util.Optional;

public interface HistoryQueryRepository {

    Optional<History> findOpenHistoryByStrategyId(Long id);

    List<HistoryDao> findHistoryByStrategyId(Long id);
}
