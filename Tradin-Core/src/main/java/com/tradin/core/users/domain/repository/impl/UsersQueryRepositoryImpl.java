package com.tradin.core.users.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.core.users.domain.repository.UsersQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersQueryRepositoryImpl implements UsersQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


}
