package com.tradin.api.futuresOrder;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.futuresOrder.service.dto.FuturesOrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "선물 주문내역", description = "선물 주문내역 관련 API")
public interface FuturesOrderApi {

    @Operation(summary = "선물 주문내역 조회")
    public TradinResponse<FuturesOrderResponseDto> findFuturesOrdersByAccountId(@AuthenticationPrincipal Long userId, @PathVariable Long accountId);

}
