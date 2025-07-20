package com.tradin.core.users.service;


import com.tradin.core.account.implement.AccountProcessor;
import com.tradin.core.auth.service.dto.UserDataDto;
import com.tradin.core.users.domain.UserSocialType;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.implement.UsersProcessor;
import com.tradin.core.users.implement.UsersReader;
import com.tradin.core.users.service.dto.FindUserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersReader usersReader;
    private final UsersProcessor usersProcessor;
    private final AccountProcessor accountProcessor;

    public Users saveOrGetUser(UserDataDto userDataDto, UserSocialType socialType) {
        if (isUserExist(userDataDto.getEmail())) {
            return readUserByEmail(userDataDto);
        }
        return createUserAndAccount(userDataDto, socialType);
    }

    public FindUserInfoResponseDto findUserInfo(Long userId) {
        Users user = usersReader.findById(userId);
        return new FindUserInfoResponseDto(user.getName(), user.getEmail());
    }

    private Users createUserAndAccount(UserDataDto userDataDto, UserSocialType socialType) {
        Users user = usersProcessor.createUser(
            userDataDto.getName(),
            userDataDto.getSub(),
            userDataDto.getEmail(),
            userDataDto.getSocialId(),
            socialType
        );
        accountProcessor.createAccountAndUsdtBalance(user);

        return user;
    }

    private Users readUserByEmail(UserDataDto userDataDto) {
        return usersReader.findByEmail(userDataDto.getEmail());
    }

    private boolean isUserExist(String email) {
        return usersReader.isUserExist(email);
    }
}
