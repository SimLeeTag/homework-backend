package com.simleetag.homework.api.domain.oauth;

import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;
import com.simleetag.homework.api.utils.ControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuthControllerTest extends ControllerTest {

    @Autowired
    private OAuthController OAuthController;

    @Test
    @DisplayName("OAuth 로그인 테스트")
    void loginTest() throws Exception {

        // given
        final String accessToken = "aaa.bbb.ccc";
        final TokenResponse tokenResponse = new TokenResponse(accessToken);
        given(oAuthService.signUpOrLogin(any(TokenRequest.class))).willReturn(tokenResponse);

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/oauth")
                        .param("code", "test-code")
                        .param("providerType", "KAKAO")
        );

        // then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.accessToken").value(accessToken));
    }
}
