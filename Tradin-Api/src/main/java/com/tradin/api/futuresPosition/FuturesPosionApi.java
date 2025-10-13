package com.tradin.api.futuresPosition;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.futuresPosition.service.dto.FuturesPositionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "선물 포지션", description = "선물 포지션 관련 API")
public interface FuturesPosionApi {

    @Operation(summary = "선물 포지션 조회")
    TradinResponse<FuturesPositionResponseDto> findFuturesPositionByAccountId(@AuthenticationPrincipal Long userId, @PathVariable Long accountId);
}
