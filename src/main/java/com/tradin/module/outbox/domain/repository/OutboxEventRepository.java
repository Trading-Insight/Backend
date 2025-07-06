package com.tradin.module.outbox.domain.repository;

import com.tradin.module.outbox.domain.OutboxEvent;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long>, OutboxEventQueryRepository {

    Optional<OutboxEvent> findByEventId(String eventId);
    
    List<OutboxEvent> findByEventIdIn(Set<String> eventIds);
}