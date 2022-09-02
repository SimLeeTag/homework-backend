package com.simleetag.homework.api.domain.user.api;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;
import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest extends TestSupport {

    @Autowired
    private OAuthJwt oauthJwt;

    @Nested
    class findUserTest {

        @Test
        @DisplayName("AccessToken을 가진 유저 조회 테스트")
        void findUserByAccessToken() throws Exception {

            final var oauthController = new OAuthControllerFlow(defaultMockMvc, false);
            final var userController = new UserControllerFlow(defaultMockMvc, false);
            final var homeController = new HomeControllerFlow(defaultMockMvc, false);

            // given
            // 유저 생성
            var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
            final TokenResponse ever = oauthController.login(loginRequest);
            final TokenResponse poogle = oauthController.login(loginRequest);
            final TokenResponse lena = oauthController.login(loginRequest);
            final TokenResponse ttozzi = oauthController.login(loginRequest);

            // 정보 수정
            final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
            final UserProfileRequest poogleProfile = new UserProfileRequest("푸글", "https://image.com");
            final UserProfileRequest lenaProfile = new UserProfileRequest("레나", "https://image.com");
            final UserProfileRequest ttozziProfile = new UserProfileRequest("또치", "https://image.com");

            userController.editProfile(ever.homeworkToken(), everProfile);
            userController.editProfile(poogle.homeworkToken(), poogleProfile);
            userController.editProfile(lena.homeworkToken(), lenaProfile);
            userController.editProfile(ttozzi.homeworkToken(), ttozziProfile);

            // 집 생성
            final CreatedHomeResponse backend = homeController.createHome(ever.homeworkToken(), new CreateHomeRequest("백엔드"));
            final CreatedHomeResponse iOS = homeController.createHome(lena.homeworkToken(), new CreateHomeRequest("iOS"));

            // 집 입장
            homeController.joinHome(backend.homeId(), poogle.homeworkToken());
            homeController.joinHome(iOS.homeId(), ttozzi.homeworkToken());
            homeController.joinHome(iOS.homeId(), ever.homeworkToken());

            // when
            final UserWithHomesResponse response =
                    new UserControllerFlow(restDocsMockMvc).findUserByAccessToken(ever.homeworkToken());

            // then
            assertThat(response.homes().size()).isEqualTo(2);
        }

        @Test
        @DisplayName("AccessToken에 해당하는 유저가 존재하지 않을 경우 테스트")
        void findUserByAccessNotExistToken() throws Exception {

            // given
            final long userId = 10L;
            final String homeworkToken = oauthJwt.createHomeworkToken(userId);
            final String message = String.format("UserID[%d]에 해당하는 유저가 존재하지 않습니다.", userId);

            // when
            final String response =
                    new UserControllerFlow(restDocsMockMvc).findUserByAccessTokenFail(homeworkToken, status().isBadRequest());

            // then
            assertThat(response).isEqualTo(message);
        }
    }

    @Nested
    class editProfileTest {

        @Test
        @DisplayName("유저 프로필 정보를 수정한다.")
        void editProfile() throws Exception {

            // given
            final var loginRequest = new TokenRequest("sample.homework.token", ProviderType.APPLE);
            final TokenResponse loginUser = new OAuthControllerFlow(defaultMockMvc, false).login(loginRequest);

            final var profileRequest = new UserProfileRequest("에버", "https://image.com");

            // when
            final UserProfileResponse response = new UserControllerFlow(restDocsMockMvc).editProfile(loginUser.homeworkToken(), profileRequest);

            // then
            assertThat(response.userName()).isEqualTo("에버");
        }
    }
}
