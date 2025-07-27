package com.tradin.core.account.service;

import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.core.balance.service.BalanceService;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.account.domain.Account;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.service.UsersService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountFacadeService {
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final UsersService usersService;

    @Transactional
    public void createAccount(Long userId) {
        Users user = usersService.findById(userId);
        Account account = accountService.createAccount(user);
        balanceService.createUsdtBalance(account);
    }

    @Transactional(readOnly = true)
    public AccountsResponseDto getAccounts(Long userId) {
        return accountService.getAccounts(userId);
    }

    @Transactional
    public void faucet(Long userId, Long accountId) {
        accountService.findAccountByIdAndUserId(accountId, userId);
        balanceService.updateBalance(accountId, CoinType.USDT, BigDecimal.valueOf(10000));
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsByIds (List<Long> accountIds) {
        return accountService.findAllByIds(accountIds);
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountService.findById(id);
    }
}
