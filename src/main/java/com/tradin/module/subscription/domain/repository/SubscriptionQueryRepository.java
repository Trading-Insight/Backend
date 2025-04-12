package com.tradin.module.subscription.domain.repository;

import com.tradin.module.subscription.domain.repository.dao.SubscriptionsDao;
import java.util.List;

public interface SubscriptionQueryRepository {

    List<SubscriptionsDao> findAllByUserIdAndAccountId(Long accountId);
}
