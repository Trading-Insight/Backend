package com.tradin.core.account.service.dto;


import java.util.List;

public record AccountsResponseDto(List<AccountDto> accounts) {

    public static AccountsResponseDto of(List<AccountDto> accounts) {
        return new AccountsResponseDto(accounts);
    }
}
