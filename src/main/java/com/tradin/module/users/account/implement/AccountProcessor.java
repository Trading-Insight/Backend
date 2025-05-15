package com.tradin.module.users.account.implement;


import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.domain.repository.AccountRepository;
import com.tradin.module.users.balance.implement.BalanceProcessor;
import com.tradin.module.users.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProcessor {

    private final BalanceProcessor balanceProcessor;
    private final AccountRepository accountRepository;

    public Account createAccountAndUsdtBalance(Users user) {
        Account account = Account.of(user);
        balanceProcessor.createUsdtBalance(account);
        return accountRepository.save(account);

    }
}
