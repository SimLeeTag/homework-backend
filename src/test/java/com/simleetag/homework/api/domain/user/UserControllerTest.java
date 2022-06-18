package com.simleetag.homework.api.domain.user;

import java.util.List;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

    @MockBean
    private UserService userService;

    @MockBean
    private HomeService homeService;

    @MockBean
    private OAuthJwt oauthJwt;

    @Test
    @DisplayName("AccessToken을 가진 유저 조회 테스트")
    void findUserByAccessToken() throws Exception {

        // given
        final User user = UserResources.aFixtureWithNoMembers();
        given(userService.findById(any(Long.class))).willReturn(user);

        final Home home = HomeResources.aFixtureWithMembers();
        given(homeService.findAllWithMembers(any(Long.class))).willReturn(List.of(home));
        given(oauthJwt.parseClaimsAsLoginUser(any(String.class))).willReturn(new LogInUser(1L));

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/users/me")
                        .with(successMockMvc.userToken())
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("AccessToken에 해당하는 유저가 존재하지 않을 경우 테스트")
    void findUserByAccessNotExistToken() throws Exception {

        // given
        final String message = String.format("UserID[%d]에 해당하는 유저가 존재하지 않습니다.", 1L);
        given(userService.findById(any(Long.class))).willThrow(new IllegalArgumentException(message));
        given(oauthJwt.parseClaimsAsLoginUser(any(String.class))).willReturn(new LogInUser(1L));

        // when
        ResultActions resultActions = this.failMockMvc.perform(
                get("/users/me")
                        .with(failMockMvc.userToken())
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    @DisplayName("유효하지 않은 AccessToken 테스트")
    void accessTokenIsNotValidTest() throws Exception {

        // given
        given(oauthJwt.parseClaimsAsLoginUser(any(String.class))).willThrow(new JWTDecodeException("Invalid JWT"));

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/users/me")
                        .with(failMockMvc.userToken())
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(jsonPath("$.message").value("잘못된 JWT 토큰입니다."));
    }
}
