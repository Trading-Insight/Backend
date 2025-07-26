package com.tradin.core.auth.service;

import static com.tradin.core.users.domain.UserSocialType.GOOGLE;

import com.tradin.core.account.domain.Account;
import com.tradin.core.account.service.AccountService;
import com.tradin.core.auth.service.dto.TokenReissueDto;
import com.tradin.core.auth.service.dto.TokenResponseDto;
import com.tradin.core.auth.service.dto.UserDataDto;
import com.tradin.core.balance.service.BalanceService;
import com.tradin.core.users.domain.Users;
import com.tradin.core.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthFacadeService {
    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
    private final UsersService usersService;
    private final AccountService accountService;
    private final BalanceService balanceService;

    @Transactional
    public void createTestUsers() {
        String email = authService.generateRandomEmail();
        String generatedString = email.substring(0, email.indexOf("@"));
        
        UserDataDto userDataDto = UserDataDto.of("TEST", generatedString, email, generatedString);
        saveOrGetUser(userDataDto);
    }

    @Transactional
    public TokenResponseDto auth(String code) {
        UserDataDto userDataDto = googleAuthService.getUserInfo(code);
        Users user = saveOrGetUser(userDataDto);
        
        return authService.createToken(user);
    }

    @Transactional
    public TokenResponseDto reissueToken(TokenReissueDto request) {
        return authService.reissueToken(request);
    }

    @Transactional
    public TokenResponseDto issueTestToken(Long userId) {
        return authService.issueTestToken(userId);
    }

    private Users saveOrGetUser(UserDataDto userDataDto) {
        Users user = usersService.saveOrGetUser(
            userDataDto.getName(),
            userDataDto.getSub(),
            userDataDto.getEmail(),
            userDataDto.getSocialId(),
            GOOGLE
        );

        Account account = accountService.createAccount(user);
        balanceService.createUsdtBalance(account);
        return user;
    }
}
