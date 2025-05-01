package com.tradin.module.strategy.subscription.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SubscriptionStatus {
    ACTIVE("ACTIVE", "활성"),
    INACTIVE("INACTIVE", "비활성"),
    PAUSED("PAUSED", "중단");

    private final String code;
    private final String name;

    public Boolean isActivated() {
        return this == ACTIVE;
    }

    public Boolean isDeActivated() {
        return this == INACTIVE;
    }

    public Boolean isPaused() {
        return this == PAUSED;
    }
}
