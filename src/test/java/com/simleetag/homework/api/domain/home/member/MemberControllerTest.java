package com.simleetag.homework.api.domain.home.member;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest extends TestSupport {

    private HomeControllerFlow homeController;

    private MemberControllerFlow memberController;

    private String homeworkToken;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;

    private Long userId;


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

        userId = ever.user().userId();
        homeworkToken = ever.homeworkToken();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(ever.homeworkToken(), everProfile);
    }


    @Test
    @DisplayName("집 들어가기 성공 테스트")
    void joinHome() throws Exception {

        // given
        // 집 생성
        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        final CreatedHomeResponse home = homeController.createHome(homeworkToken, request);

        // when
        memberController.joinHome(home.homeId(), homeworkToken);

        // then
        final HomeWithMembersResponse joinedHome = homeController.findMembersByToken(homeworkToken, home.invitation());
        final boolean joined = joinedHome.members()
                                         .stream()
                                         .anyMatch(member -> member.userId().equals(userId));

        assertThat(joined).isTrue();
    }
}
