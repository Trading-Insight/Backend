package com.tradin.core.futuresPosition.service;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.balance.domain.vo.Amount;
import com.tradin.core.common.annotation.DistributedLock;
import com.tradin.core.futuresPosition.domain.FuturesPosition;
import com.tradin.core.futuresPosition.service.dto.FuturesPositionResponseDto;
import com.tradin.core.strategy.domain.CoinType;
import com.tradin.core.strategy.domain.TradingType;
import com.tradin.core.price.domain.vo.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuturesPositionFacadeService {
    private final AccountService accountService;
    private final FuturesPositionService futuresPositionService;

    @Transactional(readOnly = true)
    public FuturesPositionResponseDto findOpenFuturesPositionByAccountId(Long userId, Long accountId) {
        accountService.findAccountByIdAndUserId(accountId, userId);
        return FuturesPositionResponseDto.of(futuresPositionService.findOpenFuturesPositionByAccountId(accountId));
    }
}
