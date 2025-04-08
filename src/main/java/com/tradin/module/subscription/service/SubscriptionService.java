package com.tradin.module.subscription.service;


import com.tradin.module.subscription.implement.SubscriptionReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionReader subscriptionReader;

}
