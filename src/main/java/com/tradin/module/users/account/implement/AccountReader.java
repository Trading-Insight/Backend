package com.tradin.module.users.account.implement;


import static com.tradin.common.exception.ExceptionType.NOT_FOUND_ACCOUNT_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.NOT_FOUND_BALANCE_EXCEPTION;

import com.tradin.common.exception.TradinException;
import com.tradin.module.futures.position.domain.FuturesPosition;
import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.domain.repository.AccountRepository;
import com.tradin.module.users.account.service.dto.AccountDto;
import com.tradin.module.users.balance.domain.Balance;
import java.util.List;
import java.util.Optional;
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

    public Account fetchAccountByIdAndUserId(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TradinException(NOT_FOUND_ACCOUNT_EXCEPTION));
    }

    public Optional<Balance> getOptionalBalanceByAccountAndCoinType(Account account, CoinType coinType) {
        return account.getBalanceByCoinType(coinType);
    }

    public Balance getBalanceByAccountAndCoinType(Account account, CoinType coinType) {
        return account.getBalanceByCoinType(coinType).orElseThrow(() -> new TradinException(NOT_FOUND_BALANCE_EXCEPTION));
    }

    public Optional<FuturesPosition> getOpenFuturesPositionByAccountAndCoinType(Account account, CoinType coinType) {
        return account.getOpenFuturesPositionByCoinType(coinType);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
