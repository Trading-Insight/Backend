package com.tradin.common.fixture;

import com.tradin.module.users.users.domain.UserSocialType;
import com.tradin.module.users.users.domain.Users;

public class UserFixture {

    /**
     * 기본 Users 생성 (활성 상태)
     */
    public static Users createDefaultUser() {
        return Users.of(
            "Test User",
            "sub-test-user",
            "email@email.com",
            "socialId-test-user",
            UserSocialType.GOOGLE
        );
    }

    /**
     * 이메일로 Users 생성
     */
    public static Users createUserWithEmail(String email) {
        return Users.of(
            "Test User",
            "sub-" + email,
            email,
            "socialId",
            UserSocialType.GOOGLE
        );
    }
}
