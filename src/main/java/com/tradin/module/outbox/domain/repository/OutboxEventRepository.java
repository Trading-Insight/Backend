package com.tradin.module.outbox.domain.repository;

import com.tradin.module.outbox.domain.OutboxEvent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long>, OutboxEventQueryRepository {

    Optional<OutboxEvent> findByEventId(String eventId);
}