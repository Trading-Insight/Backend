package com.tradin.core.outbox.domain.repository.impl;


import static com.tradin.core.outbox.domain.QOutboxMessage.outboxMessage;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxStatus;
import com.tradin.core.outbox.domain.repository.OutboxMessageQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxMessageQueryRepositoryImpl implements OutboxMessageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OutboxMessage> findAllByStatus(OutboxStatus status) {
        return jpaQueryFactory
            .selectFrom(outboxMessage)
            .where(outboxMessage.status.eq(status))
            .fetch();
    }

    @Override
    public List<OutboxMessage> findAllByMessageIdIn(List<String> messageIds) {
        return jpaQueryFactory
            .selectFrom(outboxMessage)
            .where(outboxMessage.messageId.in(messageIds))
            .fetch();
    }

    @Override
    public void markAllAsCompleted(List<Long> ids) {
        jpaQueryFactory
            .update(outboxMessage)
            .set(outboxMessage.status, OutboxStatus.COMPLETED)
            .where(outboxMessage.id.in(ids))
            .execute();
    }


    @Override
    public void markAllAsPublished(List<Long> ids) {
        jpaQueryFactory
            .update(outboxMessage)
            .set(outboxMessage.status, OutboxStatus.PUBLISHED)
            .where(outboxMessage.id.in(ids))
            .execute();
    }

} 