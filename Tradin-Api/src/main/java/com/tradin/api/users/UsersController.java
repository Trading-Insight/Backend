package com.tradin.api.users;

import com.tradin.core.users.service.UserFacadeService;
import com.tradin.core.users.service.dto.FindUserInfoResponseDto;
import com.tradin.api.common.response.TradinResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")
public class UsersController implements UsersApi {

    private final UserFacadeService userFacadeService;

    @GetMapping("/me")
    public TradinResponse<FindUserInfoResponseDto> findUserInfo(@AuthenticationPrincipal Long userId) {
        return TradinResponse.success(userFacadeService.findUserInfo(userId));
    }
}
