package com.tradin.module.users.domain.repository;

import com.tradin.module.users.domain.Users;

import java.util.List;

public interface UsersQueryRepository {
    List<Users> findByAutoTradingSubscriber(String name);
}
