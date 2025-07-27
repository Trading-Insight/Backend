package com.tradin.core.fixture;


import com.tradin.core.account.domain.Account;

public class AccountFixture {

    /**
     * 기본 Account 생성
     */
    public static Account createDefaultAccount() {
        return Account.of(UserFixture.createDefaultUser());
    }
}
