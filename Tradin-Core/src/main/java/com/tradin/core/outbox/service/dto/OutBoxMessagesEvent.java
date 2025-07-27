package com.tradin.core.outbox.service.dto;

import com.tradin.core.outbox.domain.OutboxMessage;
import java.util.List;

public record OutBoxMessagesEvent (
    List<OutboxMessage> outboxMessages
) {
    public OutBoxMessagesEvent(List<OutboxMessage> outboxMessages) {
        this.outboxMessages = outboxMessages;
    }

    public List<OutboxMessage> getOutboxMessages() {
        return outboxMessages;
    }
}