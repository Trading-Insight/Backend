package com.tradin.core.auth.service;

import static com.tradin.core.users.domain.UserSocialType.GOOGLE;

import com.tradin.core.auth.service.dto.TokenReissueDto;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import com.tradin.core.auth.service.dto.UserDataDto;
import com.tradin.core.common.jwt.JwtProvider;
import com.tradin.core.common.jwt.JwtUtil;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.service.UsersService;
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

    public TokenResponseDto issueTestToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    private TokenResponseDto createJwtToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    private Users saveOrGetUser(UserDataDto userDataDto) {
        return usersService.saveOrGetUser(userDataDto, GOOGLE);
    }

}
