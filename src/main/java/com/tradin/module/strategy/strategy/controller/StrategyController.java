package com.tradin.module.strategy.strategy.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.strategy.strategy.controller.dto.response.FindStrategiesInfoResponseDto;
import com.tradin.module.strategy.strategy.service.StrategyService;
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

}
