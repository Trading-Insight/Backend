package com.tradin.module.subscription.domain.repository;

import com.tradin.module.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, SubscriptionQueryRepository {

}
