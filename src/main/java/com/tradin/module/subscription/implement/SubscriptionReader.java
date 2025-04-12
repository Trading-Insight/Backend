package com.tradin.module.subscription.implement;

import com.tradin.module.account.implement.AccountReader;
import com.tradin.module.subscription.controller.dto.FindSubscriptionsResponseDto;
import com.tradin.module.subscription.domain.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

    private final AccountReader accountReader;
    private final SubscriptionRepository subscriptionRepository;

    public FindSubscriptionsResponseDto findSubscriptions(Long userId, Long accountId) {
        validateExistAccount(userId, accountId);
        return FindSubscriptionsResponseDto.of(subscriptionRepository.findAllByUserIdAndAccountId(accountId));
    }

    private void validateExistAccount(Long userId, Long accountId) {
        accountReader.readAccountByIdAndUserId(accountId, userId);
    }
}
