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
    public List<OutboxEvent> findAllByStatus(OutboxStatus status) {
        return jpaQueryFactory
            .selectFrom(outboxEvent)
            .where(outboxEvent.status.eq(status))
            .fetch();
    }

    @Override
    public List<OutboxEvent> findAllByEventIdIn(List<String> eventIds) {
        return jpaQueryFactory
            .selectFrom(outboxEvent)
            .where(outboxEvent.eventId.in(eventIds))
            .fetch();
    }

    @Override
    public void markAllAsCompleted(List<Long> ids) {
        jpaQueryFactory
            .update(outboxEvent)
            .set(outboxEvent.status, OutboxStatus.COMPLETED)
            .where(outboxEvent.id.in(ids))
            .execute();
    }


    @Override
    public void markAllAsPublished(List<Long> ids) {
        jpaQueryFactory
            .update(outboxEvent)
            .set(outboxEvent.status, OutboxStatus.PUBLISHED)
            .where(outboxEvent.id.in(ids))
            .execute();
    }

} 