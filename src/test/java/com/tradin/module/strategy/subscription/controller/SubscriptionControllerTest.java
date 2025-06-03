package com.tradin.module.strategy.subscription.controller;

import com.tradin.common.utils.BaseControllerTest;
import com.tradin.module.strategy.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

class SubscriptionControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubscriptionService subscriptionService;
//
//    @Test
//    @DisplayName("구독 목록 조회 - 성공")
//    @WithMockUser(username = "1", roles = "USER")
//    void getSubscriptions_Success() throws Exception {
//        // when
//        ResultActions result = mockMvc.perform(get("/v1/subscriptions")
//            .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"))
//            .andExpect(jsonPath("$.data").isArray())
//            .andExpect(jsonPath("$.data[0].id").value(testSubscription.getId()))
//            .andExpect(jsonPath("$.data[0].status").value(testSubscription.getStatus().toString()));
//    }
//
//    @Test
//    @DisplayName("구독 활성화 - 성공")
//    @WithMockUser(username = "1", roles = "USER")
//    void activateSubscription_Success() throws Exception {
//        // given
//        testSubscription.deactivate();
//        subscriptionRepository.save(testSubscription);
//
//        // when
//        ResultActions result = mockMvc.perform(post(
//            "/v1/subscriptions/activate/accounts/strategies/{strategyId}",
//            testStrategy.getId()
//        )
//            .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//
//        // DB 검증
//        Subscription updated = subscriptionRepository.findById(testSubscription.getId()).orElseThrow();
//        assertThat(updated.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
//    }
//
//    @Test
//    @DisplayName("구독 비활성화 - 성공")
//    @WithMockUser(username = "1", roles = "USER")
//    void deactivateSubscription_Success() throws Exception {
//        // when
//        ResultActions result = mockMvc.perform(post(
//            "/v1/subscriptions/deactivate/accounts/strategies/{strategyId}",
//            testStrategy.getId()
//        )
//            .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//
//        // DB 검증
//        Subscription updated = subscriptionRepository.findById(testSubscription.getId()).orElseThrow();
//        assertThat(updated.getStatus()).isEqualTo(SubscriptionStatus.INACTIVE);
//    }
//
//    @Test
//    @DisplayName("계정별 구독 목록 조회 - 성공")
//    @WithMockUser(username = "1", roles = "USER")
//    void getSubscriptionsByAccount_Success() throws Exception {
//        // when
//        ResultActions result = mockMvc.perform(get("/v1/subscriptions/accounts/{accountId}", testAccount.getId())
//            .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"))
//            .andExpect(jsonPath("$.data").isArray())
//            .andExpect(jsonPath("$.data[0].accountId").value(testAccount.getId()));
//    }
//
//    @Test
//    @DisplayName("전략별 구독 목록 조회 - 성공")
//    @WithMockUser(username = "1", roles = "USER")
//    void getSubscriptionsByStrategy_Success() throws Exception {
//        // when
//        ResultActions result = mockMvc.perform(get("/v1/subscriptions/strategies/{strategyId}", testStrategy.getId())
//            .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        result.andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"))
//            .andExpect(jsonPath("$.data").isArray())
//            .andExpect(jsonPath("$.data[0].strategyId").value(testStrategy.getId()));
//    }
}