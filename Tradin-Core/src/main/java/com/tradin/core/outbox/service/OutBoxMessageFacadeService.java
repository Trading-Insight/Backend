package com.tradin.core.outbox.service;

import com.tradin.core.outbox.domain.OutboxMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutBoxMessageFacadeService {
    private final OutBoxMessageService outBoxMessageService;

    @Transactional(readOnly = true)
    public List<OutboxMessage> findAllPendingEvents() {
        return outBoxMessageService.findAllPendingEvents();
    }

    @Transactional(readOnly = true)
    public OutboxMessage findByEventId(String eventId) {
        return outBoxMessageService.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<OutboxMessage> findByEventUuids(List<String> eventUuids) {
        return outBoxMessageService.findByMessageUuids(eventUuids);
    }

    @Transactional
    public void markAsCompleted(OutboxMessage outboxMessage) {
        outBoxMessageService.markAsCompleted(outboxMessage);
    }

    @Transactional
    public void markAllAsCompleted(List<Long> ids) {
        outBoxMessageService.markAllAsCompleted(ids);
    }

    @Transactional
    public void markAsProcessingFailed(OutboxMessage outboxMessage, String errorMessage) {
        outBoxMessageService.markAsProcessingFailed(outboxMessage, errorMessage);
    }
}
