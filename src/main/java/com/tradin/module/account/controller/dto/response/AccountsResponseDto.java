package com.tradin.module.account.controller.dto.response;


public record AccountsResponseDto(Long id, String name) {

    public static AccountsResponseDto of(Long id, String name) {
        return new AccountsResponseDto(id, name);
    }
}
