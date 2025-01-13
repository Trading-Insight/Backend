package com.tradin.module.history.controller;

import com.tradin.common.response.TradinResponse;
import com.tradin.module.history.controller.dto.request.BackTestRequestDto;
import com.tradin.module.history.controller.dto.response.BackTestResponseDto;
import com.tradin.module.history.service.HistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/histories")
public class HistoryController implements HistoryApi {

    private final HistoryService historyService;

    @GetMapping("")
    public TradinResponse<BackTestResponseDto> backTest(
        //TODO - TIMEZONE UTC로 통일하기
        @Valid @ModelAttribute BackTestRequestDto request, Pageable pageable) {
        return TradinResponse.success(historyService.backTest(request.toServiceDto(), pageable));
    }
}
