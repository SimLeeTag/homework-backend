package com.simleetag.homework.api.domain.user;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.member.MemberResources;
import com.simleetag.homework.api.domain.member.MemberService;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private HomeService homeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OAuthJwt oauthJwt;

    @Test
    @DisplayName("AccessToken을 가진 유저 조회 테스트")
    void findUserByAccessToken() throws Exception {

        // given
        final User user = userService.save(UserResources.aFixtureWithNoMembers());
        final Home home = homeService.save(HomeResources.aFixtureWithNoMembers());
        memberService.save(MemberResources.aFixture(null, user, home));

        final String accessToken = oauthJwt.createAccessToken(1L);

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/users/me")
                        .with(successMockMvc.userToken(accessToken))
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("AccessToken에 해당하는 유저가 존재하지 않을 경우 테스트")
    void findUserByAccessNotExistToken() throws Exception {

        // given
        final long userId = 10L;
        final String message = String.format("UserID[%d]에 해당하는 유저가 존재하지 않습니다.", userId);
        final String accessToken = oauthJwt.createAccessToken(userId);

        // when
        ResultActions resultActions = this.failMockMvc.perform(
                get("/users/me")
                        .with(failMockMvc.userToken(accessToken))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    @DisplayName("유효하지 않은 AccessToken 테스트")
    void accessTokenIsNotValidTest() throws Exception {

        // given

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                get("/users/me")
                        .with(failMockMvc.userToken())
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                     .andExpect(jsonPath("$.message").value("잘못된 JWT 토큰입니다."));
    }
}
