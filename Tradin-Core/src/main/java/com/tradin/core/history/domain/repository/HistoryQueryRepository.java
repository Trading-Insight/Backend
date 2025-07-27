package com.tradin.core.history.domain.repository;


import com.tradin.core.history.domain.History;
import com.tradin.core.history.domain.repository.dao.HistoryDao;
import java.util.List;
import java.util.Optional;

public interface HistoryQueryRepository {

    Optional<History> findOpenHistoryByStrategyId(Long id);

    List<HistoryDao> findHistoryDaosByStrategyId(Long id);
}
