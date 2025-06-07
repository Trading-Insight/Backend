package com.tradin.module.outbox.domain;

public enum OutboxStatus {
    PENDING,
    PUBLISHED,
    FAILED,
    COMPLETED
} 