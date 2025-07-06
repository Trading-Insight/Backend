package com.tradin.module.outbox.domain;

public enum OutboxStatus {
    PENDING,
    PUBLISHING,
    PUBLISHED,
    PUBLISHING_FAILED,
    COMPLETED,
    PROCESSING_FAILED;
} 