package com.simleetag.homework.api.domain.home.api;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.MemberControllerFlow;
import com.simleetag.homework.api.domain.home.member.MemberMaintenanceControllerFlow;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
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

public class HomeMaintenanceControllerTest extends TestSupport {

    private String everHomeworkToken;

    private HomeControllerFlow homeController;

    private HomeMaintenanceControllerFlow homeMaintenanceController;

    private MemberControllerFlow memberController;

    private MemberMaintenanceControllerFlow memberMaintenanceController;

    private OAuthControllerFlow oauthController;

    private String poogleHomeworkToken;

    private UserControllerFlow userController;

    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        memberController = new MemberControllerFlow(mockMvc);
        homeMaintenanceController = new HomeMaintenanceControllerFlow(mockMvc);
        memberMaintenanceController = new MemberMaintenanceControllerFlow(mockMvc);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 에버 유저 생성
        var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
        final TokenResponse ever = oauthController.login(loginRequest);
        everHomeworkToken = ever.homeworkToken();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(ever.homeworkToken(), everProfile);

        // 푸글 유저 생성
        final TokenResponse poogle = oauthController.login(loginRequest);
        poogleHomeworkToken = poogle.homeworkToken();

        // 푸글 정보 수정
        final UserProfileRequest poogleProfile = new UserProfileRequest("푸글", "https://image.com");
        userController.editProfile(poogle.homeworkToken(), poogleProfile);
    }

    @Test
    @DisplayName("집에 속한 모든 유저를 내쫓기 테스트")
    void kickOutAllTest() throws Exception {

        // given
        // 집 생성
        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        final CreatedHomeResponse response = homeController.createHome(everHomeworkToken, request);

        // 집 들어가기
        final MemberIdResponse memberIdResponse = memberController.joinHome(response.homeId(), poogleHomeworkToken);

        // when
        homeMaintenanceController.kickOutAll(response.homeId());

        // then
        final MemberResponse memberResponse = memberMaintenanceController.findOne(response.homeId(), memberIdResponse.memberId());
        assertThat(memberResponse.deletedAt()).isNotNull();
    }
}
