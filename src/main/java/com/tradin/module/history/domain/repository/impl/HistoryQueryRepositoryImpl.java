package com.tradin.module.history.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.history.domain.History;
import com.tradin.module.history.domain.repository.HistoryQueryRepository;
import com.tradin.module.history.domain.repository.dao.HistoryDao;
import com.tradin.module.history.domain.repository.dao.QHistoryDao;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.tradin.module.history.domain.QHistory.history;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<History> findLastHistoryByStrategyId(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(history)
                        .where(history.strategy.id.eq(id))
                        .orderBy(history.entryPosition.time.desc())
                        .fetchFirst()
        );
    }

    @Override
    public List<HistoryDao> findHistoryDaoByStrategyId(Long id) {
        return jpaQueryFactory.select(new QHistoryDao(history.id, history.entryPosition, history.exitPosition,
                        history.profitRate))
                .from(history)
                .where(history.strategy.id.eq(id))
                .orderBy(history.id.asc())
                .fetch();
    }
}
