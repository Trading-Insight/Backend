package com.tradin.module.strategy.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/strategies")
public class StrategyController implements StrategyApi {
    private final StrategyService strategyService;

    @GetMapping("/future")
    public TradinResponse<FindStrategiesInfoResponseDto> findFutureStrategiesInfos() {
        return TradinResponse.success(strategyService.findFutureStrategiesInfo());
    }

    @GetMapping("/spot")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyService.findSpotStrategiesInfo();
    }

//    @Operation(summary = "선물 전략 구독 리스트")
//    @GetMapping("/subscriptions")
//    public FindSubscriptionStrategiesInfoResponseDto findSubscriptionStrategiesInfos() {
//        return strategyService.findSubscriptionStrategiesInfo();
//    }
//
//    @Operation(summary = "선물 전략 구독")
//    @PostMapping("/{id}/subscriptions")
//    public void subscribe(@Valid @RequestBody SubscribeStrategyRequestDto request, @PathVariable Long id) {
//        strategyService.subscribeStrategy(request.toServiceDto(id));
//    }
//
//    @Operation(summary = "선물 전략 구독 취소")
//    @PatchMapping("/unsubscriptions")
//    public void unsubscribe(@Valid @RequestBody UnSubscribeStrategyRequestDto request) {
//        strategyService.unsubscribeStrategy(request.toServiceDto());
//    }
}
