package com.tradin.module.history.controller;

import com.tradin.common.annotation.DisableAuthInSwagger;
import com.tradin.module.history.controller.dto.request.BackTestRequestDto;
import com.tradin.module.history.controller.dto.response.BackTestResponseDto;
import com.tradin.module.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/histories")
public class HistoryController {
    private final HistoryService historyService;

    @Operation(summary = "백테스트")
    @DisableAuthInSwagger
    @GetMapping("")
    public BackTestResponseDto backTest(@Valid @ModelAttribute BackTestRequestDto request) {
        return historyService.backTest(request.toServiceDto());
    }
}
