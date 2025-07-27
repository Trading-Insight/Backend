package com.tradin.api.account;

import com.tradin.core.account.service.AccountFacadeService;
import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.api.common.response.TradinResponse;

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

    private final AccountFacadeService accountFacadeService;

    @PostMapping("")
    public TradinResponse<String> createAccount(@AuthenticationPrincipal Long userId) {
        accountFacadeService.createAccount(userId);
        return TradinResponse.success();
    }

    @GetMapping()
    public TradinResponse<AccountsResponseDto> getAccounts(@AuthenticationPrincipal Long userId) {
        return TradinResponse.success(accountFacadeService.getAccounts(userId));
    }

    @PostMapping("/{accountId}/faucet")
    public void faucet(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        accountFacadeService.faucet(userId, accountId);
    }
}
