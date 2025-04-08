package com.tradin.module.subscription.controller;

import com.tradin.module.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


}
