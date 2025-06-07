package com.tradin.module.outbox.domain;

import com.tradin.common.exception.ExceptionType;
import com.tradin.common.exception.TradinException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxEventType {
    AUTO_TRADE("auto-trade-topic", "자동 매매 이벤트");

    private final String topic;
    private final String description;

    public static OutboxEventType fromTopic(String topic) {
        for (OutboxEventType type : values()) {
            if (type.getTopic().equals(topic)) {
                return type;
            }
        }
        throw new TradinException(ExceptionType.NOT_FOUND_SUCH_METHOD_EXCEPTION, topic);
    }
} 