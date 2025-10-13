package com.tradin.api.balance;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.balance.service.BalanceFacadeService;
import com.tradin.core.balance.service.dto.BalanceResponseDto;
import com.tradin.core.strategy.domain.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BalanceController implements BalanceApi {
    private final BalanceFacadeService balanceFacadeService;

    @GetMapping("/accounts/{accountId}/balances")
    public TradinResponse<BalanceResponseDto> findBalancesByAccountId(Long userId, Long accountId) {
        return TradinResponse.success(balanceFacadeService.findBalancesByAccountId(userId, accountId));
    }

}
