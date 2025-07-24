package com.tradin.core.account.service;



import com.tradin.core.account.domain.Account;
import com.tradin.core.account.implement.AccountProcessor;
import com.tradin.core.account.implement.AccountReader;
import com.tradin.core.account.service.dto.AccountDto;
import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.implement.BalanceProcessor;
import com.tradin.core.balance.implement.BalanceReader;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.implement.UsersReader;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tradin.core.balance.domain.vo.Money;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountReader accountReader;
    private final UsersReader usersReader;
    private final BalanceReader balanceReader;
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
        Account account = accountReader.findAccountByIdAndUserId(accountId, userId);
        Balance usdtBalance = balanceReader.findByAccountIdAndCoinType(account.getId(), CoinType.USDT);
        balanceProcessor.updateBalance(usdtBalance, Money.of(BigDecimal.valueOf(10000)));
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
