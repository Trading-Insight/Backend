package com.tradin.core.outbox.domain;

import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxMessageType {
    AUTO_TRADE("auto-trade-topic", "자동 매매 메시지");

    private final String topic;
    private final String description;

    public static OutboxMessageType fromTopic(String topic) {
        for (OutboxMessageType type : values()) {
            if (type.getTopic().equals(topic)) {
                return type;
            }
        }
        throw new TradinException(ExceptionType.NOT_FOUND_SUCH_METHOD_EXCEPTION, topic);
    }
} 