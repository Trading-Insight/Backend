package com.tradin.core.account.domain.repository;



import com.tradin.core.account.domain.Account;
import com.tradin.core.account.service.dto.AccountDto;
import java.util.List;

public interface AccountQueryRepository {

    //Optional<Account> fetchByIdAndUserId(Long id, Long userId);
    List<AccountDto> findAccountDtosByUserId(Long userId);

    List<Account> findSubscribedAccountsByStrategyId(Long strategyId);
}
