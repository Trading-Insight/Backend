package com.tradin.core.outbox.implement.dto;

import com.tradin.core.outbox.domain.OutboxMessage;
import java.util.List;
import lombok.Getter;

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