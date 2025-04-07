package com.tradin.module.account.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.account.controller.dto.response.AccountsResponseDto;
import com.tradin.module.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/auto-trading/activate")
    public TradinResponse<String> activateAutoTrading(@AuthenticationPrincipal Long userId, Long accountId) {
        accountService.activateAutoTrading(userId, accountId);
        return TradinResponse.success();
    }

    @PostMapping("/auto-trading/deactivate")
    public TradinResponse<String> deactivateAutoTrading(@AuthenticationPrincipal Long userId, Long accountId) {
        accountService.deactivateAutoTrading(userId, accountId);
        return TradinResponse.success();
    }
}
