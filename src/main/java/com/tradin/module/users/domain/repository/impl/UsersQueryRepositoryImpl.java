package com.tradin.module.users.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.strategy.domain.QStrategy;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.domain.repository.UsersQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tradin.module.users.domain.QUsers.users;

@RequiredArgsConstructor
public class UsersQueryRepositoryImpl implements UsersQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Users> findByAutoTradingSubscriber(String name) {
        return jpaQueryFactory.selectFrom(users)
                .innerJoin(users.strategy, QStrategy.strategy)
                .where(QStrategy.strategy.name.eq(name))
                .fetch();
    }
}
