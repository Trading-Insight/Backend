package com.tradin.module.users.account.service;


import com.tradin.module.strategy.strategy.domain.CoinType;
import com.tradin.module.users.account.controller.dto.response.AccountsResponseDto;
import com.tradin.module.users.account.domain.Account;
import com.tradin.module.users.account.implement.AccountProcessor;
import com.tradin.module.users.account.implement.AccountReader;
import com.tradin.module.users.account.service.dto.AccountDto;
import com.tradin.module.users.balance.domain.Balance;
import com.tradin.module.users.balance.implement.BalanceProcessor;
import com.tradin.module.users.users.domain.Users;
import com.tradin.module.users.users.implement.UsersReader;
import java.math.BigDecimal;
import java.util.List;
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
    private final UsersReader usersReader;
    private final AccountProcessor accountProcessor;
    private final BalanceProcessor balanceProcessor;

    @Transactional
    public void createAccount(Long userId) {
        createAccountByUserId(userId);
    }

    public AccountsResponseDto getAccounts(Long userId) {
        return readAccountsByUserId(userId);
    }

    @Transactional
    public void faucet(Long userId, Long accountId) {
        Account account = accountReader.fetchAccountByIdAndUserId(accountId, userId);
        Balance usdtBalance = accountReader.getBalanceByAccountAndCoinType(account, CoinType.USDT);
        balanceProcessor.updateBalance(usdtBalance, BigDecimal.valueOf(10000));
    }

    private AccountsResponseDto readAccountsByUserId(Long userId) {
        List<AccountDto> accounts = accountReader.findAccountDtosByUserId(userId);
        return AccountsResponseDto.of(accounts);
    }

    private void createAccountByUserId(Long userId) {
        Users user = readUserById(userId);
        createAccount(user);
    }

    private void createAccount(Users user) {
        accountProcessor.createAccountAndUsdtBalance(user);
    }

    private Users readUserById(Long userId) {
        return usersReader.findById(userId);
    }

}
