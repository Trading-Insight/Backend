package com.tradin.api.futuresPosition;


import com.tradin.core.futuresPosition.service.FuturesPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/futures/positions")
public class FuturesPositionController {

    private final FuturesPositionService futuresPositionService;
}
