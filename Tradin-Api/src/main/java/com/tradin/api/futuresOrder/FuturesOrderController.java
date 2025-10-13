package com.tradin.api.futuresOrder;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.futuresOrder.service.dto.FuturesOrderResponseDto;
import com.tradin.core.futuresOrder.service.FuturesOrderFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FuturesOrderController implements FuturesOrderApi {

    private final FuturesOrderFacadeService futuresOrderFacadeService;

    @GetMapping("/accounts/{accountId}/futures/orders")
    public TradinResponse<FuturesOrderResponseDto> findFuturesOrdersByAccountId(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        return TradinResponse.success(futuresOrderFacadeService.findFuturesOrdersByAccountId(userId, accountId));
    }
}
