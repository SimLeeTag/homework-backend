package com.simleetag.homework.api.domain.home.member;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;
import com.simleetag.homework.api.domain.user.api.UserControllerFlow;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberMaintenanceControllerTest extends TestSupport {

    private CreatedHomeResponse home;

    private HomeControllerFlow homeController;

    private MemberMaintenanceControllerFlow memberMaintenanceController;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;

    private Long userId;

    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        memberMaintenanceController = new MemberMaintenanceControllerFlow(mockMvc);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 에버 유저 생성
        var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
        final TokenResponse ever = oauthController.login(loginRequest);

        userId = ever.user().userId();
        final String homeworkToken = ever.homeworkToken();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(ever.homeworkToken(), everProfile);

        // 집 생성
        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        home = homeController.createHome(homeworkToken, request);
    }


    @Test
    @DisplayName("집 들어가기(멤버 생성) 테스트")
    void join() throws Exception {

        // when
        final MemberResponse response = memberMaintenanceController.join(home.homeId(), userId);

        // then
        assertThat(response.userId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("집 나가기(멤버 삭제) 테스트")
    void leave() throws Exception {

        // when
        final MemberResponse response = memberMaintenanceController.join(home.homeId(), userId);

        // then
        assertThat(response.userId()).isEqualTo(userId);
    }
}
