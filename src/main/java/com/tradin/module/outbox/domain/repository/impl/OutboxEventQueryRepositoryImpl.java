package com.tradin.module.outbox.domain.repository.impl;

import static com.tradin.module.outbox.domain.QOutboxEvent.outboxEvent;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxStatus;
import com.tradin.module.outbox.domain.repository.OutboxEventQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxEventQueryRepositoryImpl implements OutboxEventQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OutboxEvent> findAllByStatusOrderByCreatedAtAsc(OutboxStatus status) {
        return jpaQueryFactory
            .selectFrom(outboxEvent)
            .where(outboxEvent.status.eq(status))
            .orderBy(outboxEvent.createdAt.asc())
            .fetch();
    }
} 