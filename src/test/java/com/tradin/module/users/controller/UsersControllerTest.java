package com.tradin.module.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradin.common.config.TestContainerConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(TestContainerConfiguration.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    @WithMockUser
//    void API_AND_SECRET_KEY_유효성_테스트() throws Exception {
//        String binanceApiKey = "5abe3b3ac2e84742e3bcc4f15077d595cb782f6e8457c14c6e1c420b4ca277eb";
//        String binanceSecretKey = "ebf63090b614608d051d0dfca05c1c117a362be9a5b80f28027fd27bc48de797";
//
//        mockMvc.perform(post("/v1/users/binance/ping")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(new PingRequestDto(binanceApiKey, binanceSecretKey)))
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void API_AND_SECRET_KEY_잘못_입력_테스트() throws Exception {
//        String binanceApiKey = "WRONG_API_KEY";
//        String binanceSecretKey = "WRONG_SECRET_KEY";
//
//        mockMvc.perform(post("/v1/users/binance/ping")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(new PingRequestDto(binanceApiKey, binanceSecretKey)))
//                ).andDo(print())
//                .andExpect(status().is5xxServerError());
//    }

//    @Test
//    @WithMockUser(username = "1")
//    public void 레버리지_수량_포지션_타입_변경_테스트() throws Exception {
//        mockMvc.perform(post("/v1/users/binance/metadata")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(new ChangeMetadataRequestDto(1, 100, TradingType.BOTH)))
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }
}
