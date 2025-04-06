package com.tradin.module.users.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.controller.dto.response.FindUserInfoResponseDto;
import com.tradin.module.users.service.UsersService;
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

//    @Operation(summary = "Api&Secret Key 유효성 검사")
//    @PostMapping("/binance/ping")
//    public ResponseEntity<String> ping(@Valid @RequestBody PingRequestDto request) {
//        return ResponseEntity.ok(usersService.ping(request.toServiceDto()));
//    }
//
//    @Operation(summary = "레버리지, 수량, 포지션 타입 변경")
//    @PostMapping("/binance/metadata")
//    public ResponseEntity<String> changeMetaData(@Valid @RequestBody ChangeMetadataRequestDto request) {
//        return ResponseEntity.ok(usersService.changeMetaData(request.toServiceDto()));
//    }

}
