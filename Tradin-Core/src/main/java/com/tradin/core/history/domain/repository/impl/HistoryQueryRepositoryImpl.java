package com.tradin.core.history.domain.repository.impl;



import static com.tradin.core.history.domain.QHistory.history;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.core.history.domain.History;
import com.tradin.core.history.domain.repository.HistoryQueryRepository;
import com.tradin.core.history.domain.repository.dao.HistoryDao;
import com.tradin.core.history.domain.repository.dao.QHistoryDao;
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
    public List<HistoryDao> findHistoryDaosByStrategyId(Long id) {
        return jpaQueryFactory.select(new QHistoryDao(history.id, history.entryPosition, history.exitPosition,
                history.profitRate
            ))
            .from(history)
            .where(history.strategy.id.eq(id))
            .fetch();
    }
}
