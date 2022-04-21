package com.simleetag.homework.controller;

import com.simleetag.homework.dto.TokenRequest;
import com.simleetag.homework.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @Autowired
    private UserController userController;

    @Test
    @DisplayName("")
    void loginTest() throws Exception {

        // given
        final String accessToken = "aaa.bbb.ccc";
        final String refreshToken = "xxx.yyy.zzz";
        final TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        given(oAuthService.signUpOrLogin(any(TokenRequest.class))).willReturn(tokenResponse);

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/oauth")
                        .param("code", "test-code")
                        .param("providerType", "kakao")
        ).andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.accessToken").value(accessToken))
                     .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }
}
