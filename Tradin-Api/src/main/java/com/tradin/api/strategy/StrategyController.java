package com.tradin.api.strategy;

import com.tradin.api.strategy.dto.WebHookRequestDto;
import com.tradin.api.common.response.TradinResponse;

import com.tradin.core.strategy.service.StrategyService;
import com.tradin.core.strategy.service.dto.FindStrategiesInfoResponseDto;
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
