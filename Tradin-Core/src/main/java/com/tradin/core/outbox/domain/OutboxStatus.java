package com.tradin.core.outbox.domain;

public enum OutboxStatus {
    PENDING,
    PUBLISHING,
    PUBLISHED,
    PUBLISHING_FAILED,
    COMPLETED,
    PROCESSING_FAILED;
} 