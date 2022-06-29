package com.simleetag.homework.api.domain.oauth;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.infra.provider.ProviderType;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StableOAuthTest extends IntegrationTest {

    @Test
    @DisplayName("카카오에서 유효하지 않은 AccessToken 테스트")
    void accessTokenIsNotValidTest() throws Exception {

        // given
        String requestBody = objectMapper.writeValueAsString(new TokenRequest("access.token.sample", ProviderType.KAKAO));

        // when
        ResultActions resultActions = this.failMockMvc.perform(
                post("/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                     .andExpect(jsonPath("$.message").value("{\"msg\":\"this access token does not exist\",\"code\":-401}"));
    }

    @Test
    @DisplayName("애플에서 유효하지 않은 AccessToken 테스트")
    void accessTokenIsNotValidAtAppleTest() throws Exception {

        // given
        String requestBody = objectMapper.writeValueAsString(new TokenRequest("access.token.sample", ProviderType.APPLE));

        // when
        ResultActions resultActions = this.failMockMvc.perform(
                post("/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                     .andExpect(jsonPath("$.message").value("잘못된 JWT 토큰입니다."));
    }
}
