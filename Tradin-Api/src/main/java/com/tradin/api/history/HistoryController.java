package com.tradin.api.history;

import com.tradin.api.history.dto.BackTestRequestDto;
import com.tradin.core.history.service.HistoryService;
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

    private final HistoryService historyService;

    @GetMapping("")
    public TradinResponse<BackTestResponseDto> backTest(
        @Valid @ModelAttribute BackTestRequestDto request, Pageable pageable) {
        return TradinResponse.success(historyService.backTest(request.toServiceDto(), pageable));
    }

    @Operation(summary = "테스트 히스토리 생성")
    @PostMapping("/{strategyId}")
    public void createHistory(@PathVariable Long strategyId) {
        historyService.createHistory(strategyId);
    }

}
