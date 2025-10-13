package com.tradin.api.test;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.api.strategy.dto.WebHookRequestDto;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "테스트", description = "테스트용 API")
public interface TestApi {

    @Operation(summary = "테스트 토큰 발급")
    public TradinResponse<TokenResponseDto> issueTestToken(@PathVariable Long userId);

    @Operation(summary = "테스트 유저 생성")
    public TradinResponse<Void> createTestUsers(@RequestParam Long count);

    @Operation(summary = "테스트 전략 생성")
    public void createTestStrategy();

    @Operation(summary = "테스트 히스토리 생성")
    public void createTestHistory();

    @Operation(summary = "테스트 전략 전체 구독")
    public TradinResponse<String> activateAutoTradingTest(@PathVariable Long strategyId);

    @Operation(summary = "테스트 웹훅")
    public void handleWebHook();

    @Operation(summary = "테스트 전략 전체 구독 해제")
    public TradinResponse<String> deactivateAutoTradingTest(@PathVariable Long strategyId);

}
