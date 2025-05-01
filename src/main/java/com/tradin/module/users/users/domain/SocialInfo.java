package com.tradin.module.users.users.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SocialInfo {
    @Column(nullable = false, length = 200)
    private String socialId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserSocialType socialType;

    private SocialInfo(String socialId, UserSocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }

    public static SocialInfo of(String socialId, UserSocialType socialType) {
        return new SocialInfo(socialId, socialType);
    }
}
