package com.tradin.api.history;

import com.tradin.api.history.dto.BackTestRequestDto;
import com.tradin.core.history.service.HistoryFacadeService;
import com.tradin.core.history.service.dto.BackTestResponseDto;
import com.tradin.api.common.response.TradinResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/histories")
public class HistoryController implements HistoryApi {

    private final HistoryFacadeService historyFacadeService;

    @GetMapping("")
    public TradinResponse<BackTestResponseDto> backTest(
        @Valid @ModelAttribute BackTestRequestDto request, Pageable pageable) {
        return TradinResponse.success(historyFacadeService.backTest(request.toServiceDto(), pageable));
    }

    @Operation(summary = "테스트 히스토리 생성")
    @PostMapping("/{strategyId}")
    public void createTestHistory(@PathVariable Long strategyId) {
        historyFacadeService.createTestHistory(strategyId);
    }

}
