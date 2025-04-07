package com.tradin.module.users.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradin.module.users.domain.repository.UsersQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersQueryRepositoryImpl implements UsersQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


}
