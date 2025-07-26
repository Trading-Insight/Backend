package com.tradin.api.strategy;

import com.tradin.api.strategy.dto.WebHookRequestDto;
import com.tradin.api.common.response.TradinResponse;

import com.tradin.core.strategy.service.StrategyFacadeService;
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

    private final StrategyFacadeService strategyFacadeService;

    @GetMapping("/futures")
    public TradinResponse<FindStrategiesInfoResponseDto> findFutureStrategiesInfos() {
        return TradinResponse.success(strategyFacadeService.findFutureStrategiesInfo());
    }

    @GetMapping("/spots")
    public FindStrategiesInfoResponseDto findSpotStrategiesInfos() {
        return strategyFacadeService.findSpotStrategiesInfo();
    }

    //Test API
    @PostMapping("")
    public void createStrategy() {
        strategyFacadeService.createStrategy();
    }

    @PostMapping("/futures/short-term/webhook")
    public void handleFutureShortTermV1WebHook(@RequestBody @Valid WebHookRequestDto request) {
        strategyFacadeService.handleFutureWebHook(request.toServiceDto());
    }
}
