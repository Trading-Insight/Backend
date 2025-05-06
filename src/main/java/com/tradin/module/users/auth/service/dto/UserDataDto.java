package com.tradin.module.users.auth.service.dto;

import com.tradin.module.users.users.domain.UserSocialType;
import com.tradin.module.users.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDataDto {

    private final String name;
    private final String sub;
    private final String email;
    private final String socialId;

    public static UserDataDto of(String name, String sub, String email, String socialId) {
        return new UserDataDto(name, sub, email, socialId);
    }

    public Users toEntity(UserSocialType socialType) {
        return Users.builder()
            .name(name)
            .sub(sub)
            .email(email)
            .socialId(socialId)
            .socialType(socialType)
            .build();
    }
}
