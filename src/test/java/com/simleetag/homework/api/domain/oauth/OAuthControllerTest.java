package com.simleetag.homework.api.domain.oauth;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;
import com.simleetag.homework.api.domain.oauth.infra.provider.ProviderType;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuthControllerTest extends IntegrationTest {

    @MockBean
    private OAuthService oauthService;

    @Test
    @DisplayName("OAuth 로그인 테스트")
    void loginTest() throws Exception {

        // given
        final String accessToken = "sample.access.token";
        final TokenResponse tokenResponse = new TokenResponse(accessToken);
        given(oauthService.signUpOrLogin(any(TokenRequest.class))).willReturn(tokenResponse);

        String requestBody = objectMapper.writeValueAsString(new TokenRequest("access.token.sample", ProviderType.KAKAO));

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                post("/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.accessToken").value(accessToken));
    }
}
