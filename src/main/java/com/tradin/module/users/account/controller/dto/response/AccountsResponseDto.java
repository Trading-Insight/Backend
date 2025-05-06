package com.tradin.module.users.account.controller.dto.response;


import com.tradin.module.users.account.service.dto.AccountDto;
import java.util.List;

public record AccountsResponseDto(List<AccountDto> accounts) {

    public static AccountsResponseDto of(List<AccountDto> accounts) {
        return new AccountsResponseDto(accounts);
    }
}
