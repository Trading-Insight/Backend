package com.tradin.common.fixture;

import com.tradin.module.users.account.domain.Account;

public class AccountFixture {

    /**
     * 기본 Account 생성
     */
    public static Account createDefaultAccount() {
        return Account.of(UserFixture.createDefaultUser());
    }
}
