package com.tradin.module.outbox.domain.repository;

import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxStatus;
import java.util.List;

public interface OutboxEventQueryRepository {

    List<OutboxEvent> findAllByStatus(OutboxStatus status);
} 