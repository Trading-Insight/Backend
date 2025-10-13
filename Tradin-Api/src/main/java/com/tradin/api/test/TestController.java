package com.tradin.api.test;

import static com.tradin.core.strategy.domain.CoinType.BTC;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.api.strategy.dto.WebHookRequestDto;
import com.tradin.core.auth.service.AuthFacadeService;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import com.tradin.core.common.annotation.DisableAuthInSwagger;
import com.tradin.core.history.service.HistoryFacadeService;
import com.tradin.core.price.domain.PriceCache;
import com.tradin.core.strategy.domain.Position;
import com.tradin.core.strategy.domain.Strategy;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.strategy.service.StrategyFacadeService;
import com.tradin.core.subscription.service.SubscriptionFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tests")
public class TestController implements TestApi {
    private final AuthFacadeService authFacadeService;
    private final HistoryFacadeService historyFacadeService;
    private final SubscriptionFacadeService subscriptionFacadeService;
    private final StrategyFacadeService strategyFacadeService;
    private final PriceCache priceCache;

    @DisableAuthInSwagger
    @PostMapping("/auth/token/{userId}")
    public TradinResponse<TokenResponseDto> issueTestToken(@PathVariable Long userId) {
        return TradinResponse.success(authFacadeService.issueTestToken(userId));
    }

    @DisableAuthInSwagger
    @PostMapping("/auth/users")
    public TradinResponse<Void> createTestUsers(@RequestParam Long count) {
        for (int i = 0; i < count; i++) {
            authFacadeService.createTestUsers();
        }
        return TradinResponse.success();
    }

    @PostMapping("/strategies")
    public void createTestStrategy() {
        strategyFacadeService.createTestStrategy();
    }

    @PostMapping("/histories")
    public void createTestHistory() {
        historyFacadeService.createTestHistory();
    }


    @PostMapping("/subscriptions/accounts/strategies/{strategyId}")
    public TradinResponse<String> activateAutoTradingTest(@PathVariable Long strategyId) {
        subscriptionFacadeService.activateSubscriptionTest(strategyId);
        return TradinResponse.success();
    }

    @PostMapping("/webhooks/tradingview/signals")
    public void handleWebHook() {
        Strategy strategy = strategyFacadeService.findStrategyById(1L);

        Position newPosition = strategy.getCurrentPosition().isShort()
            ? Position.of(TradingType.LONG, LocalDateTime.now(), priceCache.getPrice(BTC))
            : Position.of(TradingType.SHORT, LocalDateTime.now(), priceCache.getPrice(BTC));

        WebHookRequestDto request = new WebHookRequestDto(1L, newPosition);

        strategyFacadeService.publishAutoTradingMessages(request.toServiceDto());
        strategyFacadeService.updateStrategyAndHistoryMetaData(request.toServiceDto());
    }

    @DeleteMapping("/subscriptions/accounts/strategies/{strategyId}")
    public TradinResponse<String> deactivateAutoTradingTest(@PathVariable Long strategyId) {
        subscriptionFacadeService.deActivateSubscriptionTest(strategyId);
        return TradinResponse.success();
    }
}
