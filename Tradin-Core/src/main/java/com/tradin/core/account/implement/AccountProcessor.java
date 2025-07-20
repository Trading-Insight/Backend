package com.tradin.core.account.implement;



import com.tradin.core.account.domain.Account;
import com.tradin.core.account.domain.repository.AccountRepository;
import com.tradin.core.balance.implement.BalanceProcessor;
import com.tradin.core.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProcessor {

    private final BalanceProcessor balanceProcessor;
    private final AccountRepository accountRepository;

    public Account createAccountAndUsdtBalance(Users user) {
        Account account = accountRepository.save(Account.of(user));
        balanceProcessor.createUsdtBalance(account);
        return account;

    }
}
