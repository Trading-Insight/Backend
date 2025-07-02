package com.tradin.module.outbox.implement;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import com.tradin.module.outbox.domain.OutboxEvent;
import com.tradin.module.outbox.domain.OutboxStatus;
import com.tradin.module.outbox.domain.repository.OutboxEventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventReader {

    private final OutboxEventRepository outboxEventRepository;

    public List<OutboxEvent> findAllPendingEvents() {
        return outboxEventRepository.findAllByStatus(OutboxStatus.PENDING);
    }

    public OutboxEvent findByEventId(String eventId) {
        return outboxEventRepository.findByEventId(eventId)
            .orElseThrow(() -> new TradinException(ExceptionType.NOT_FOUND_OUTBOX_EVENT_EXCEPTION));
    }
}
