package com.tradin.core.balance.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.balance.domain.Balance;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.balance.service.dto.BalanceResponseDto;
import com.tradin.core.strategy.domain.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceFacadeService {
    private final AccountService accountService;
    private final BalanceService balanceService;

    @Transactional(readOnly = true)
    public BalanceResponseDto findBalancesByAccountId(Long userId, Long accountId) {
        accountService.findAccountByIdAndUserId(accountId, userId);
        return BalanceResponseDto.of(balanceService.findBalanceDaosByAccountId(accountId));
    }
}
