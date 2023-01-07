package com.simleetag.homework.api.domain.home.member;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberControllerTest extends TestSupport {

    private MemberControllerFlow memberController;

    private HomeControllerFlow homeController;

    private String homeworkToken;

    private Long userId;

    private Long homeId;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;


    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        memberController = new MemberControllerFlow(mockMvc);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 에버 유저 생성
        var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
        final TokenResponse ever = oauthController.login(loginRequest);

        homeworkToken = ever.homeworkToken();
        userId = ever.user().userId();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(ever.homeworkToken(), everProfile);

        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        final CreatedHomeResponse home = homeController.createHome(homeworkToken, request);
        homeId = home.homeId();
    }

    @Test
    @DisplayName("집 아이디와 유저 아이디로 멤버 아이디 조회 성공 테스트")
    void findMemberByHomeIdAndUserId() throws Exception {

        // given
        // 집 들어가기(멤버 추가)
        MemberIdResponse memberIdResponse = homeController.joinHome(homeId, homeworkToken);

        // when
        MemberIdResponse findMemberId = memberController.findMemberId(homeworkToken, homeId);

        // then
        assertThat(memberIdResponse.memberId()).isEqualTo(findMemberId.memberId());
    }


}
