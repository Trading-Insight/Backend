package com.tradin.module.users.account.service.dto;

import com.querydsl.core.annotations.QueryProjection;

public record AccountDto(
    Long id,
    String name
) {

    @QueryProjection
    public AccountDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AccountDto of(Long id, String name) {
        return new AccountDto(id, name);
    }
}
