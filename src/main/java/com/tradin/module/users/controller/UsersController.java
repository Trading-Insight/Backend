package com.tradin.module.users.controller;

import com.tradin.module.users.controller.dto.request.ChangeMetadataRequestDto;
import com.tradin.module.users.controller.dto.request.PingRequestDto;
import com.tradin.module.users.controller.dto.response.FindUserInfoResponseDto;
import com.tradin.module.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")

public class UsersController {
    private final UsersService usersService;

    @Operation(summary = "Api&Secret Key 유효성 검사")
    @PostMapping("/binance/ping")
    public ResponseEntity<String> ping(@Valid @RequestBody PingRequestDto request) {
        return ResponseEntity.ok(usersService.ping(request.toServiceDto()));
    }

    @Operation(summary = "레버리지, 수량, 포지션 타입 변경")
    @PostMapping("/binance/metadata")
    public ResponseEntity<String> changeMetaData(@Valid @RequestBody ChangeMetadataRequestDto request) {
        return ResponseEntity.ok(usersService.changeMetaData(request.toServiceDto()));
    }

    @Operation(summary = "유저 정보")
    @GetMapping("/me")
    public ResponseEntity<FindUserInfoResponseDto> findUserInfo() {
        return ResponseEntity.ok(usersService.findUserInfo());
    }
}
