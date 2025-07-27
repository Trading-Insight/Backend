package com.tradin.core.subscription.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.common.exception.ExceptionType;
import com.tradin.core.common.exception.TradinException;
import com.tradin.core.futuresPosition.service.FuturesPositionService;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.service.StrategyService;
import com.tradin.core.subscription.service.dto.FindSubscriptionsResponseDto;
import com.tradin.core.subscription.service.dto.ActivateSubscriptionDto;
import com.tradin.core.subscription.service.dto.DeactivateSubscriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionFacadeService {
    private final SubscriptionService subscriptionService;
    private final AccountService accountService;
    private final StrategyService strategyService;
    private final FuturesPositionService futuresPositionService;

    @Transactional(readOnly = true)
    public FindSubscriptionsResponseDto findSubscriptions(Long userId, Long accountId) {
        accountService.findAccountByIdAndUserId(accountId, userId);
        return subscriptionService.findSubscriptions(accountId);
    }

    @Transactional
    public void activateSubscription(ActivateSubscriptionDto dto) {
        Account account = accountService.findAccountByIdAndUserId(dto.getAccountId(), dto.getUserId());
        Strategy strategy = strategyService.findStrategyById(dto.getStrategyId());
        
        futuresPositionService.findOpenFuturesPositionByAccountAndCoinType(dto.getAccountId(), strategy.getCoinType())
            .ifPresent(position -> {
                throw new TradinException(ExceptionType.ALREADY_POSITION_EXIST_EXCEPTION);
            });
        
        subscriptionService.activateSubscription(account, strategy);
    }

    @Transactional
    public void deActivateSubscription(DeactivateSubscriptionDto dto) {
        Account account = accountService.findAccountByIdAndUserId(dto.getAccountId(), dto.getUserId());
        Strategy strategy = strategyService.findStrategyById(dto.getStrategyId());
        subscriptionService.deActivateSubscription(account, strategy);
    }

    @Transactional
    public void activateSubscriptionTest(Long userId, Long strategyId) {
        List<Account> accounts = accountService.findAll();
        Strategy strategy = strategyService.findStrategyById(strategyId);
        subscriptionService.activateSubscriptionTest(accounts, strategy);
    }

    @Transactional
    public void deActivateSubscriptionTest(Long userId, Long strategyId) {
        List<Account> accounts = accountService.findAll();
        Strategy strategy = strategyService.findStrategyById(strategyId);
        subscriptionService.deActivateSubscriptionTest(accounts, strategy);
    }
}
