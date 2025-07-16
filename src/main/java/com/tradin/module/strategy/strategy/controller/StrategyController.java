package com.tradin.module.strategy.strategy.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.strategy.strategy.controller.dto.request.WebHookRequestDto;
import com.tradin.module.strategy.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.strategy.service.StrategyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/strategies")
public class StrategyController implements StrategyApi {

    private final StrategyService strategyService;

    @GetMapping("/futures")
    public TradinResponse<FindStrategiesInfoResponseDto> findFutureStrategiesInfos() {
        return TradinResponse.success(strategyService.findFutureStrategiesInfo());
    }

    @GetMapping("/spots")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyService.findSpotStrategiesInfo();
    }

    //Test API
    @PostMapping("")
    public void createStrategy() {
        strategyService.createStrategy();
    }

    @PostMapping("/futures/short-term/webhook")
    public void handleFutureShortTermV1WebHook(@RequestBody @Valid WebHookRequestDto request) {
        strategyService.handleFutureWebHook(request.toServiceDto());
    }
}
