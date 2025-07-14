package com.tradin.module.outbox.domain.repository;

import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxStatus;
import java.util.List;

public interface OutboxEventQueryRepository {

    List<OutboxEvent> findAllByStatus(OutboxStatus status);

    List<OutboxEvent> findAllByEventIdIn(List<String> eventIds);

    void markAllAsCompleted(List<Long> ids);

    void markAllAsPublished(List<Long> ids);
} 