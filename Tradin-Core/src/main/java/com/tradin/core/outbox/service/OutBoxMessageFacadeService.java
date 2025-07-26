package com.tradin.core.outbox.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.futuresOrder.event.dto.PositionDto;
import com.tradin.core.outbox.domain.OutboxMessage;
import com.tradin.core.strategy.domain.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutBoxMessageFacadeService {
    private final OutBoxMessageService outBoxMessageService;

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void publishAutoTradingEvents(Strategy strategy, List<Account> accounts, PositionDto position) {
        outBoxMessageService.publishAutoTradingEvents(strategy, accounts, position);
    }

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
        return outBoxMessageService.findByEventUuids(eventUuids);
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
