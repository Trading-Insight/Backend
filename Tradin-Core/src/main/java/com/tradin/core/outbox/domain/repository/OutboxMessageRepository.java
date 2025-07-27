package com.tradin.core.outbox.domain.repository;


import com.tradin.core.outbox.domain.OutboxMessage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long>, OutboxMessageQueryRepository {

    Optional<OutboxMessage> findByMessageId(String messageId);
}