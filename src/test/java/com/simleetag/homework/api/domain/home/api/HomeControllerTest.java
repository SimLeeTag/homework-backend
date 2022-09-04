package com.simleetag.homework.api.domain.home.api;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.HomeJwt;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.user.api.UserControllerFlow;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerTest extends TestSupport {

    private HomeControllerFlow homeController;
    @Autowired
    private HomeJwt homeJwt;
    private String homeworkToken;
    private OAuthControllerFlow oauthController;
    private UserControllerFlow userController;
    private Long userId;


    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 에버 유저 생성
        var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
        final TokenResponse ever = oauthController.login(loginRequest);

        userId = ever.user().userId();
        homeworkToken = ever.homeworkToken();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(ever.homeworkToken(), everProfile);

        // 푸글 유저 생성
        final TokenResponse poogle = oauthController.login(loginRequest);

        // 푸글 정보 수정
        final UserProfileRequest poogleProfile = new UserProfileRequest("푸글", "https://image.com");
        userController.editProfile(poogle.homeworkToken(), poogleProfile);
    }

    @Nested
    class createHomeTest {

        @Test
        @DisplayName("집 생성 성공 테스트")
        void createHome() throws Exception {

            // given
            final String homeName = "백엔드집";
            final CreateHomeRequest request = new CreateHomeRequest(homeName);

            // when
            final CreatedHomeResponse response = homeController.createHome(homeworkToken, request);

            // then
            assertThat(response.homeName()).isEqualTo(homeName);
        }

        @Test
        @DisplayName("최대 소속 가능 집 개수를 초과할 때 집 생성 테스트")
        void createHomeMoreThanThree() throws Exception {

            // given
            // 집 생성
            final String homeName = "백엔드집";
            final CreateHomeRequest request = new CreateHomeRequest(homeName);
            final String message = "최대 3개의 집에 소속될 수 있습니다.";

            // when
            homeController.createHome(homeworkToken, request);
            homeController.createHome(homeworkToken, request);
            homeController.createHome(homeworkToken, request);

            final String response =
                    homeController.createHomeFail(homeworkToken, request, status().isBadRequest());

            // then
            assertThat(response).isEqualTo(message);
        }
    }

    @Nested
    class getHomeInformationTest {

        @Test
        @DisplayName("집 조회 성공 테스트")
        void getHome() throws Exception {

            //given
            // 집 생성
            final String homeName = "백엔드집";
            final CreateHomeRequest request = new CreateHomeRequest(homeName);
            final CreatedHomeResponse home = homeController.createHome(homeworkToken, request);

            // 멤버 추가
            homeController.joinHome(home.homeId(), homeworkToken);

            //when
            final HomeResponse response =
                    homeController.findMembersByToken(homeworkToken, home.invitation());

            //then
            assertThat(response.homeId()).isEqualTo(home.homeId());
        }

        @Test
        @DisplayName("invitation 정보에 들어있는 home id가 존재하지 않을 경우 테스트")
        void findHomeByHomeIdNotExist() throws Exception {

            // given
            final Long homeId = 10L;
            final String invalidInvitationToken = homeJwt.createHomeworkToken(homeId);
            final String message = String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId);

            // when
            final String response =
                    homeController.findMembersByTokenFail(homeworkToken, invalidInvitationToken, status().isBadRequest());

            //then
            assertThat(response).isEqualTo(message);
        }

    }

    @Nested
    class joinHomeTest {

        @Test
        @DisplayName("집 들어가기 성공 테스트")
        void joinHome() throws Exception {

            // given
            // 집 생성
            final String homeName = "백엔드집";
            final CreateHomeRequest request = new CreateHomeRequest(homeName);
            final CreatedHomeResponse home = homeController.createHome(homeworkToken, request);

            // when
            final MemberIdResponse response = homeController.joinHome(home.homeId(), homeworkToken);

            // then
            assertThat(response.memberId()).isEqualTo(userId);
        }
    }
}
