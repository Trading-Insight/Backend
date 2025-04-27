package com.tradin.module.futures.order.controller;

import com.tradin.module.futures.order.service.FuturesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/futures/orders")
public class FuturesOrderController {

    private final FuturesOrderService futuresOrderService;
}
