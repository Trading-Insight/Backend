package com.tradin.api.strategy;

import com.tradin.api.common.response.TradinResponse;
import com.tradin.core.strategy.service.dto.FindStrategiesInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "전략", description = "전략 관련 API")
public interface StrategyApi {

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공")
  })
  @Operation(summary = "선물 전략 조회")
  TradinResponse<FindStrategiesInfoResponseDto> findFutureStrategiesInfos();

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공")
  })
  @Operation(summary = "현물 전략 조회")
  FindStrategiesInfoResponseDto findSpotStrategiesInfos();

}
