package com.tradin.module.account.service;


import com.tradin.module.account.controller.dto.response.AccountsResponseDto;
import com.tradin.module.account.domain.Account;
import com.tradin.module.account.implement.AccountProcessor;
import com.tradin.module.account.implement.AccountReader;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.implement.UsersReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountReader accountReader;
    private final AccountProcessor accountProcessor;
    private final UsersReader usersReader;

    @Transactional
    public void createAccount(Long userId) {
        createAccountByUserId(userId);
    }

    public AccountsResponseDto getAccounts(Long userId) {
        return readAccountsByUserId(userId);
    }


    private AccountsResponseDto readAccountsByUserId(Long userId) {
        Account account = accountReader.readAccountByUserId(userId);
        return AccountsResponseDto.of(account.getId(), account.getName());
    }

    private Account readAccountByIdAndUserId(Long id, Long userId) {
        return accountReader.findAccountByIdAndUserId(id, userId);
    }

    private void createAccountByUserId(Long userId) {
        Users user = readUserById(userId);
        createAccount(user);
    }

    private void createAccount(Users user) {
        accountProcessor.createAccount(user);
    }

    private Users readUserById(Long userId) {
        return usersReader.findById(userId);
    }

}
