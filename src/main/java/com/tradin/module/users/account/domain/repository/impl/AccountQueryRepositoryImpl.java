package com.tradin.module.users.account.domain.repository.impl;

import static com.tradin.module.users.account.domain.QAccount.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.users.account.domain.repository.AccountQueryRepository;
import com.tradin.module.users.account.service.dto.AccountDto;
import com.tradin.module.users.account.service.dto.QAccountDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AccountDto> findAccountDtosByUserId(Long userId) {
        return jpaQueryFactory
            .select(new QAccountDto(
                account.id,
                account.name
            ))
            .from(account)
            .where(account.user.id.eq(userId))
            .fetch();
    }
}
