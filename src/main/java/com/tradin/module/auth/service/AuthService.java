package com.tradin.module.auth.service;

import static com.tradin.module.users.domain.UserSocialType.GOOGLE;

import com.tradin.common.jwt.JwtProvider;
import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.auth.controller.dto.response.TokenResponseDto;
import com.tradin.module.auth.service.dto.TokenReissueDto;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.service.UsersService;
import java.util.UUID;
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
        Users user = saveOrFindUser(userDataDto);

        return createJwtToken(user.getId());
    }

    public TokenResponseDto createJwtToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    public TokenResponseDto reissueToken(TokenReissueDto request) {
        UUID userId = jwtUtil.validateTokensAndGetUserId(request.getAccessToken(), request.getRefreshToken());
        return jwtProvider.createJwtToken(userId);
    }

    public SignOutResponseDto deleteToken(final SignOutDto request) {
        UUID userId = jwtUtil.validateTokensAndGetUserId(request.getAccessToken(), request.getRefreshToken());
        jwtRemover.deleteRefreshToken(userId);

        return SignOutResponseDto.of(userId);
    }

    private Users saveOrFindUser(UserDataDto userDataDto) {
        return usersService.saveOrFindUser(userDataDto, GOOGLE);
    }

}
