package com.tradin.core.account.service;

import com.tradin.core.account.service.dto.AccountsResponseDto;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.balance.service.BalanceService;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.account.domain.Account;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.service.UsersService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

    @DistributedLock(
        key = "'asset-lock:' + #accountId + ':USDT'",
        fallbackMethod = "handleAssetLockFallback"
    )
    @Transactional
    public void faucet(Long userId, Long accountId) {
        accountService.findAccountByIdAndUserId(accountId, userId);
        Balance balance = balanceService.findByAccountIdAndCoinType(accountId, CoinType.USDT);
        balanceService.updateBalance(balance, Amount.of(BigDecimal.valueOf(10000)));
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsByIds (List<Long> accountIds) {
        return accountService.findAllByIds(accountIds);
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountService.findById(id);
    }

    /**
     * 입금(Faucet) 처리 시 분산락 획득 실패하면 호출되는 fallback 메서드
     */
    public void handleAssetLockFallback(Long userId, Long accountId) {
        log.warn("분산락 획득 실패 - userId: {}, accountId: {}", userId, accountId);
    }
}
