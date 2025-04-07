package com.tradin.module.account.implement;


import static com.tradin.common.exception.ExceptionType.NOT_FOUND_ACCOUNT_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.NOT_FOUND_ANY_ACCOUNT_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.account.domain.Account;
import com.tradin.module.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountReader {

    private final AccountRepository accountRepository;

    public Account readAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ANY_ACCOUNT_EXCEPTION));
    }

    public Account readAccountByIdAndUserId(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }

}
