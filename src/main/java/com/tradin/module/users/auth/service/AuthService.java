package com.tradin.module.users.auth.service;

import static com.tradin.module.users.users.domain.UserSocialType.GOOGLE;

import com.tradin.common.jwt.JwtProvider;
import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.users.auth.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.auth.service.dto.TokenReissueDto;
import com.tradin.module.users.auth.service.dto.UserDataDto;
import com.tradin.module.users.users.domain.Users;
import com.tradin.module.users.users.service.UsersService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponseDto testAuth() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        String email = generatedString + "@gmail.com";

        UserDataDto userDataDto = UserDataDto.of("TEST", generatedString, email, generatedString);
        Users users = saveOrGetUser(userDataDto);

        return jwtProvider.createJwtToken(users.getId());
    }

    public TokenResponseDto auth(String code) {
        UserDataDto userDataDto = getUserInfo(code);
        Users user = saveOrGetUser(userDataDto);

        return createJwtToken(user.getId());
    }

    public TokenResponseDto reissueToken(TokenReissueDto request) {
        Long id = jwtUtil.validateTokensAndGetUserId(request.accessToken(), request.refreshToken());
        return jwtProvider.createJwtToken(id);
    }

    private UserDataDto getUserInfo(String code) {
        return googleAuthService.getUserInfo(code);
    }

    public TokenResponseDto issueTestToken() {
        return jwtProvider.createJwtToken(1100L);
    }

    private TokenResponseDto createJwtToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    private Users saveOrGetUser(UserDataDto userDataDto) {
        return usersService.saveOrGetUser(userDataDto, GOOGLE);
    }

}
