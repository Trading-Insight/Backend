package com.tradin.module.auth.service;

import com.tradin.common.jwt.JwtUtil;
import com.tradin.module.auth.service.dto.TokenReissueDto;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.feign.client.dto.cognito.TokenDto;
import com.tradin.module.feign.service.CognitoFeignService;
import com.tradin.module.users.controller.dto.response.TokenResponseDto;
import com.tradin.module.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tradin.module.users.domain.UserSocialType.GOOGLE;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UsersService usersService;
    private final CognitoFeignService cognitoFeignService;
    private final JwtUtil jwtUtil;

    public TokenResponseDto auth(String code) {
        TokenDto token = getTokenFromCognito(code);
        String idToken = token.getId_token();
        saveUser(extractUserDataFromIdToken(idToken));

        return TokenResponseDto.of(token.getAccess_token(), token.getRefresh_token());
    }

    public String reissueToken(TokenReissueDto tokenReissueDto) {
        return reissueAccessTokenFromCognito(tokenReissueDto.getRefreshToken());
    }

    private TokenDto getTokenFromCognito(String code) {
        return cognitoFeignService.getTokenFromCognito(code);
    }

    private String reissueAccessTokenFromCognito(String refreshToken) {
        return cognitoFeignService.reissueAccessTokenFromCognito(refreshToken);
    }

    private UserDataDto extractUserDataFromIdToken(String idToken) {
        return jwtUtil.extractUserDataFromIdToken(idToken);
    }

    private void saveUser(UserDataDto userDataDto) {
        usersService.saveUser(userDataDto, GOOGLE);
    }

}
