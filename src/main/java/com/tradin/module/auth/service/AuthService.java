package com.tradin.module.auth.service;

import static com.tradin.module.users.domain.UserSocialType.GOOGLE;

import com.tradin.common.jwt.JwtProvider;
import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.auth.controller.dto.response.TokenResponseDto;
import com.tradin.module.auth.service.dto.TokenReissueDto;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

    public TokenResponseDto auth(String code) {
        UserDataDto userDataDto = googleAuthService.getUserInfo(code);
        Users user = saveOrGetUser(userDataDto);

        return createJwtToken(user.getId());
    }

    public TokenResponseDto reissueToken(TokenReissueDto request) {
        Long id = jwtUtil.validateTokensAndGetUserId(request.accessToken(), request.refreshToken());
        return jwtProvider.createJwtToken(id);
    }

    public TokenResponseDto issueTestToken() {
        return jwtProvider.createJwtToken(1L);
    }

    private TokenResponseDto createJwtToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    private Users saveOrGetUser(UserDataDto userDataDto) {
        return usersService.saveOrGetUser(userDataDto, GOOGLE);
    }

}
