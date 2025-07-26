package com.tradin.core.auth.service;

import com.tradin.core.auth.service.dto.TokenReissueDto;
import com.tradin.core.auth.service.dto.TokenResponseDto;

import com.tradin.core.common.jwt.JwtProvider;
import com.tradin.core.common.jwt.JwtUtil;
import com.tradin.core.users.domain.Users;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

    public TokenResponseDto createToken(Users user) {
        return jwtProvider.createJwtToken(user.getId());
    }

    public TokenResponseDto reissueToken(TokenReissueDto request) {
        Long id = jwtUtil.validateTokensAndGetUserId(request.accessToken(), request.refreshToken());
        return jwtProvider.createJwtToken(id);
    }

    public TokenResponseDto issueTestToken(Long userId) {
        return jwtProvider.createJwtToken(userId);
    }

    public String generateRandomEmail() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        return generatedString + "@gmail.com";
    }
}
