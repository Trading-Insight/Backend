package com.tradin.module.users.account.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.account.controller.dto.response.AccountsResponseDto;
import com.tradin.module.users.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/accounts")
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @PostMapping("/new")
    public TradinResponse<String> createAccount(@AuthenticationPrincipal Long userId) {
        accountService.createAccount(userId);
        return TradinResponse.success();
    }

    @GetMapping()
    public TradinResponse<AccountsResponseDto> getAccounts(@AuthenticationPrincipal Long userId) {
        return TradinResponse.success(accountService.getAccounts(userId));
    }

    @PostMapping("/faucet/{accountId}")
    public void faucet(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        accountService.faucet(userId, accountId);
    }
}
