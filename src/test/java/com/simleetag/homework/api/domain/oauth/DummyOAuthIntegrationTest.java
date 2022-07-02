package com.simleetag.homework.api.domain.oauth;

import java.util.Optional;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeRepository;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.member.MemberResources;
import com.simleetag.homework.api.domain.member.MemberService;
import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.infra.provider.OAuthProviderFactory;
import com.simleetag.homework.api.domain.oauth.infra.provider.ProviderType;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserRepository;
import com.simleetag.homework.api.domain.user.UserResources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DummyOAuthIntegrationTest extends IntegrationTest {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private MemberService memberService;

    @MockBean
    private OAuthProviderFactory oAuthProviderFactory;

    @SpyBean
    private UserRepository userRepository;

    @Test
    @DisplayName("최초 가입 유저 OAuth 로그인 테스트")
    void loginTest() throws Exception {

        // given
        final String accessToken = "sample.homework.token";
        String requestBody = objectMapper.writeValueAsString(new TokenRequest(accessToken, ProviderType.KAKAO));
        given(oAuthProviderFactory.retrieveOAuthId(any(TokenRequest.class))).willReturn("sample-oauth-id");

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                post("/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("기가입 유저 OAuth 로그인 테스트")
    void usedUserLoginTest() throws Exception {

        // given
        final User lena = userRepository.save(UserResources.aFixtureWithNoMembers(null, "레나"));
        final User ttozzi = userRepository.save(UserResources.aFixtureWithNoMembers(null, "또치"));

        final Home ios = homeRepository.save(HomeResources.aFixtureWithNoMembers(null, "아이오에스"));
        memberService.save(MemberResources.aFixture(null, lena, ios));
        memberService.save(MemberResources.aFixture(null, ttozzi, ios));

        final String accessToken = "sample.homework.token";
        String requestBody = objectMapper.writeValueAsString(new TokenRequest(accessToken, ProviderType.KAKAO));
        given(oAuthProviderFactory.retrieveOAuthId(any(TokenRequest.class))).willReturn("12345");
        given(userRepository.findByOauthId(any(String.class))).willReturn(Optional.ofNullable(ttozzi));

        // when
        ResultActions resultActions = this.failMockMvc.perform(
                post("/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}
