package com.tradin.core.account.service;

import static com.tradin.core.common.exception.ExceptionType.NOT_FOUND_ACCOUNT_EXCEPTION;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.domain.repository.AccountRepository;
import com.tradin.core.account.service.dto.AccountDto;
import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.users.domain.Users;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Users user) {
        Account account = createAccountEntity(user);
        return accountRepository.save(account);
    }

    public AccountsResponseDto getAccounts(Long userId) {
        List<AccountDto> accounts = accountRepository.findAccountDtosByUserId(userId);
        return AccountsResponseDto.of(accounts);
    }

    public Account findAccountByIdAndUserId(Long accountId, Long userId) {
        return accountRepository.findByIdAndUserId(accountId, userId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }

    public List<Account> findAllByIds(List<Long> ids) {
        return accountRepository.findAllById(ids);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    private Account createAccountEntity(Users user) {
        return Account.of(user);
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }
}
