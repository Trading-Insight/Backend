package com.tradin.module.users.users.domain;

import com.tradin.common.jpa.AuditTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditTime implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sub;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private SocialInfo socialInfo;

    @Builder
    private Users(String name, String sub, String email, String socialId, UserSocialType socialType) {
        this.name = name;
        this.sub = sub;
        this.email = email;
        this.socialInfo = SocialInfo.of(socialId, socialType);
    }

    public static Users of(String name, String sub, String email, String socialId, UserSocialType socialType) {
        return Users.builder()
            .name(name)
            .sub(sub)
            .email(email)
            .socialId(socialId)
            .socialType(socialType)
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
