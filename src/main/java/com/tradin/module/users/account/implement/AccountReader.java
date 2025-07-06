package com.tradin.module.users.account.implement;


import static com.tradin.common.exception.ExceptionType.NOT_FOUND_ACCOUNT_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.domain.repository.AccountRepository;
import com.tradin.module.users.account.service.dto.AccountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountReader {

    private final AccountRepository accountRepository;

    public List<AccountDto> findAccountDtosByUserId(Long userId) {
        return accountRepository.findAccountDtosByUserId(userId);
    }

    public Account findAccountByIdAndUserId(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }

    public Account findAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public List<Account> findSubscribedAccountsByStrategyId(Long strategyId) {
        return accountRepository.findSubscribedAccountsByStrategyId(strategyId);
    }
}
