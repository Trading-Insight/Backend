package com.tradin.module.futures.order.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderStatus {
    OPEN("OPEN", "진입"),
    FILLED("FILLED", "완료"),
    CANCELED("CANCELED", "취소");

    private final String code;
    private final String name;

    public Boolean isOpen() {
        return this == OPEN;
    }

    public Boolean isFilled() {
        return this == FILLED;
    }

    public Boolean isCancel() {
        return this == CANCELED;
    }
}
