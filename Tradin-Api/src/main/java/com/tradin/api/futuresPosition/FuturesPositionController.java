package com.tradin.api.futuresPosition;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.futuresOrder.service.dto.FuturesOrderResponseDto;
import com.tradin.core.futuresPosition.service.FuturesPositionFacadeService;
import com.tradin.core.futuresPosition.service.dto.FuturesPositionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FuturesPositionController implements FuturesPosionApi {

    private final FuturesPositionFacadeService futuresPositionFacadeService;

    @GetMapping("/accounts/{accountId}/futures/positions")
    public TradinResponse<FuturesPositionResponseDto> findFuturesPositionByAccountId(@AuthenticationPrincipal Long userId, @PathVariable Long accountId) {
        return TradinResponse.success(futuresPositionFacadeService.findOpenFuturesPositionByAccountId(userId, accountId));
    }

}
