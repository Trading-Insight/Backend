package com.tradin.core.users.service;

import com.tradin.core.users.service.dto.FindUserInfoResponseDto;
import com.tradin.core.auth.service.dto.UserDataDto;
import com.tradin.core.users.domain.UserSocialType;
import com.tradin.core.users.domain.Users;
import com.tradin.core.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacadeService {
    private final UsersService usersService;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public FindUserInfoResponseDto findUserInfo(Long userId) {
        return usersService.findUserInfo(userId);
    }

    @Transactional
    public Users saveOrGetUser(UserDataDto userDataDto, UserSocialType socialType) {
        Users user = usersService.saveOrGetUser(
            userDataDto.getName(),
            userDataDto.getSub(),
            userDataDto.getEmail(),
            userDataDto.getSocialId(),
            socialType
        );
        
        // Create account for new user
        if (!isUserExist(userDataDto.getEmail())) {
            accountService.createAccount(user);
        }
        
        return user;
    }

    private boolean isUserExist(String email) {
        return usersService.isUserExist(email);
    }
}
