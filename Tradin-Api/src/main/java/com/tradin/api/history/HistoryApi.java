package com.tradin.api.history;

import com.tradin.api.history.dto.BackTestRequestDto;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.api.common.response.TradinResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

@Tag(name = "히스토리", description = "히스토리 관련 API")
public interface HistoryApi {

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공")
    })
    @Operation(summary = "백테스트 실행")
    TradinResponse<BackTestResponseDto> backTest(BackTestRequestDto request, Pageable pageable);

}
