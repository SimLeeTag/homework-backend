package com.simleetag.homework.api.domain.user;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeRepository;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.member.MemberResources;
import com.simleetag.homework.api.domain.member.MemberService;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.user.dto.UserProfileRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OAuthJwt oauthJwt;

    @Nested
    class findUserTest {

        @Test
        @DisplayName("AccessToken을 가진 유저 조회 테스트")
        void findUserByAccessToken() throws Exception {

            // given
            final User ever = userRepository.save(UserResources.aFixtureWithNoMembers(null, "에버"));
            final User poogle = userRepository.save(UserResources.aFixtureWithNoMembers(null, "푸글"));
            final User lena = userRepository.save(UserResources.aFixtureWithNoMembers(null, "레나"));
            final User ttozzi = userRepository.save(UserResources.aFixtureWithNoMembers(null, "또치"));

            final Home backend = homeRepository.save(HomeResources.aFixtureWithNoMembers(null, "백엔드"));
            memberService.save(MemberResources.aFixture(null, ever, backend));
            memberService.save(MemberResources.aFixture(null, poogle, backend));

            final Home ios = homeRepository.save(HomeResources.aFixtureWithNoMembers(null, "아이오에스"));
            memberService.save(MemberResources.aFixture(null, lena, ios));
            memberService.save(MemberResources.aFixture(null, ttozzi, ios));

            final String accessToken = oauthJwt.createAccessToken(ever.getId());

            // when
            ResultActions resultActions = successMockMvc.perform(
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
            ResultActions resultActions = failMockMvc.perform(
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
            ResultActions resultActions = failMockMvc.perform(
                    get("/users/me")
                            .with(failMockMvc.userToken())
            );

            // then
            resultActions.andExpect(status().isUnauthorized())
                         .andExpect(jsonPath("$.message").value("잘못된 JWT 토큰입니다."));
        }
    }

    @Nested
    class editProfileTest {

        @Test
        @DisplayName("유저 프로필 정보를 수정한다.")
        void editProfileTest() throws Exception {

            // given
            final User user = userRepository.save(UserResources.aFixtureWithNoMembers(null));
            final String accessToken = oauthJwt.createAccessToken(user.getId());
            final UserProfileRequest request = new UserProfileRequest("에버", "https://image.com");

            // when
            ResultActions resultActions = successMockMvc.perform(
                    patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(successMockMvc.userToken(accessToken))
                            .content(objectMapper.writeValueAsBytes(request))
            );

            // then
            resultActions.andExpect(status().is2xxSuccessful());
        }
    }
}
