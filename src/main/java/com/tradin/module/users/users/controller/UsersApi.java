package com.tradin.module.users.users.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.users.users.controller.dto.response.FindUserInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저", description = "유저 관련 API")
public interface UsersApi {

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공")
    })
    @Operation(summary = "유저 정보 조회")
    TradinResponse<FindUserInfoResponseDto> findUserInfo(Long userId);
}
