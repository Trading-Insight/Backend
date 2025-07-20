package com.tradin.core.outbox.implement;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;

import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.outbox.domain.OutboxStatus;
import com.tradin.core.outbox.domain.repository.OutboxMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageReader {

    private final OutboxMessageRepository outboxMessageRepository;

    public List<OutboxMessage> findAllPendingMessages() {
        return outboxMessageRepository.findAllByStatus(OutboxStatus.PENDING);
    }

    public OutboxMessage findByMessageId(String messageId) {
        return outboxMessageRepository.findByMessageId(messageId)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_OUTBOX_MESSAGE_EXCEPTION));
    }

    public List<OutboxMessage> findByMessageIds(List<String> messageIds) {
        return outboxMessageRepository.findAllByMessageIdIn(messageIds);
    }
}
