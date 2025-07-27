package com.tradin.core.outbox.domain.repository;


import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxMessageType;
import com.tradin.core.outbox.domain.OutboxStatus;
import java.util.List;

public interface OutboxMessageQueryRepository {

    List<OutboxMessage> findAllByStatus(OutboxStatus status);

    List<OutboxMessage> findAllByMessageIdIn(List<String> messageIds);

    List<OutboxMessage> findAllByTypeAndStatus(OutboxMessageType outboxMessageType, OutboxStatus status);


    void markAllAsCompleted(List<Long> ids);

    void markAllAsPublished(List<Long> ids);
} 