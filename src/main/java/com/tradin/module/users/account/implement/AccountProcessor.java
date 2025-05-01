package com.tradin.module.users.account.implement;


import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.domain.repository.AccountRepository;
import com.tradin.module.users.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProcessor {

    private final AccountRepository accountRepository;

    public void createAccount(Users user) {
        Account account = Account.builder()
            .name(user.getName())
            .user(user)
            .build();

        accountRepository.save(account);
    }
}
