package com.tradin.module.strategy.controller;

import com.tradin.common.annotation.DisableAuthInSwagger;
import com.tradin.module.strategy.controller.dto.request.SubscribeStrategyRequestDto;
import com.tradin.module.strategy.controller.dto.request.UnSubscribeStrategyRequestDto;
import com.tradin.module.strategy.controller.dto.request.WebHookRequestDto;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.controller.dto.response.FindSubscriptionStrategiesInfoResponseDto;
import com.tradin.module.strategy.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/strategies")
@Slf4j
public class StrategyController {
    private final StrategyService strategyService;

//    @KafkaListener(topics = "tradin", groupId = "trading-strategy-executors")
//    public void test(@RequestBody WebHookRequestDto request) {
//        strategyService.handleFutureWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "future-short-term-v1", groupId = "trading-strategy-executors")
//    public void handleFutureShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleFutureWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "future-mid-term-v1", groupId = "trading-strategy-executors")
//    public void handleFutureMidTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleFutureWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "future-long-term-v1", groupId = "trading-strategy-executors")
//    public void handleFutureLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleFutureWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "spot-short-term-v1", groupId = "trading-strategy-executors")
//    public void handleSpotShortTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleSpotWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "spot-mid-term-v1", groupId = "trading-strategy-executors")
//    public void handleSpotMidTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleSpotWebHook(request.toServiceDto());
//    }
//
//    @KafkaListener(topics = "spot-long-term-v1", groupId = "trading-strategy-executors")
//    public void handleSpotLongTermV1WebHook(@RequestBody WebHookRequestDto request) {
//        strategyService.handleSpotWebHook(request.toServiceDto());
//    }

    @Operation(summary = "선물 전략 전체 조회")
    @DisableAuthInSwagger
    @GetMapping("/future")
    public FindStrategiesInfoResponseDto findFutureStrategiesInfos() {
        return strategyService.findFutureStrategiesInfo();
    }

    @Operation(summary = "현물 전략 전체 조회")
    @DisableAuthInSwagger
    @GetMapping("/spot")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyService.findSpotStrategiesInfo();
    }

    @Operation(summary = "선물 전략 구독 리스트")
    @GetMapping("/subscriptions")
    public FindSubscriptionStrategiesInfoResponseDto findSubscriptionStrategiesInfos() {
        return strategyService.findSubscriptionStrategiesInfo();
    }

    @Operation(summary = "선물 전략 구독")
    @PostMapping("/{id}/subscriptions")
    public void subscribe(@Valid @RequestBody SubscribeStrategyRequestDto request, @PathVariable Long id) {
        strategyService.subscribeStrategy(request.toServiceDto(id));
    }

    @Operation(summary = "선물 전략 구독 취소")
    @PatchMapping("/unsubscriptions")
    public void unsubscribe(@Valid @RequestBody UnSubscribeStrategyRequestDto request) {
        strategyService.unsubscribeStrategy(request.toServiceDto());
    }
}
