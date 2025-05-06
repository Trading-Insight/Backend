package com.tradin.module.strategy.history.domain.repository.impl;


import static com.tradin.module.strategy.history.domain.QHistory.history;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.history.domain.History;
import com.tradin.module.strategy.history.domain.repository.HistoryQueryRepository;
import com.tradin.module.strategy.history.domain.repository.dao.HistoryDao;
import com.tradin.module.strategy.history.domain.repository.dao.QHistoryDao;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<History> findOpenHistoryByStrategyId(Long id) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(history)
                .where(history.strategy.id.eq(id)
                    .and(history.exitPosition.isNull()))
                .fetchFirst()
        );
    }

    @Override
    public List<HistoryDao> findHistoryByStrategyId(Long id) {
        return jpaQueryFactory.select(new QHistoryDao(history.id, history.entryPosition, history.exitPosition,
                history.profitRate
            ))
            .from(history)
            .where(history.strategy.id.eq(id))
            .fetch();
    }
}
