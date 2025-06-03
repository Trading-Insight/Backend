package com.tradin.module.strategy.strategy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tradin.common.utils.BaseControllerTest;
import com.tradin.module.strategy.strategy.service.StrategyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

class StrategyControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StrategyService strategyService;


    @Test
    void 선물_전략_목록_조회_성공시_200() throws Exception {
        // when & then
        mockMvc.perform(get("/v1/strategies/future")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void 웹훅_처리_성공시_200() throws Exception {
        // given
        String webhookPayload = """
            {
              "id": 1,
              "position": {
                "tradingType": "LONG",
                "time": "2025-05-06T15:30:00",
                "price": 80000
              }
            }
            """;

        // when
        mockMvc.perform(post("/v1/strategies/webhook/futures/short-term")
                .contentType(MediaType.APPLICATION_JSON)
                .content(webhookPayload))
            .andExpect(status().isOk());
    }

    @Test
    void 웹훅_요청에_id가_없을시_500() throws Exception {
        String payload = """
            {
              "position": {
                "tradingType": "LONG",
                "time": "2025-05-06T15:30:00",
                "price": 80000
              }
            }
            """;

        mockMvc.perform(post("/v1/strategies/webhook/futures/short-term")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().is5xxServerError());
    }

    @Test
    void 웹훅_요청에_position이_없을시_500() throws Exception {
        String payload = """
            {
                "id": 1
            }
            """;

        mockMvc.perform(post("/v1/strategies/webhook/futures/short-term")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().is5xxServerError());
    }
}