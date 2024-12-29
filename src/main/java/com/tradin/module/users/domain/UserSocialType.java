package com.tradin.module.users.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserSocialType {
    GOOGLE("구글");

    private final String value;
}
