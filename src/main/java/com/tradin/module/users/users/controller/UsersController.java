package com.tradin.module.users.users.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.users.controller.dto.response.FindUserInfoResponseDto;
import com.tradin.module.users.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")
public class UsersController implements UsersApi {

    private final UsersService usersService;

    @GetMapping("/me")
    public TradinResponse<FindUserInfoResponseDto> findUserInfo(@AuthenticationPrincipal Long userId) {
        return TradinResponse.success(usersService.findUserInfo(userId));
    }
}
