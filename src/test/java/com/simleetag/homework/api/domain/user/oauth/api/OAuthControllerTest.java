package com.simleetag.homework.api.domain.user.oauth.api;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthControllerTest extends TestSupport {

    @Test
    @DisplayName("최초 가입 유저 OAuth 로그인 테스트")
    void loginTest() throws Exception {

        // given
        final var request = new TokenRequest("sample.homework.token", ProviderType.KAKAO);

        // when
        final TokenResponse response = new OAuthControllerFlow(restDocsMockMvc).login(request);

        // then
        assertThat(response.user().userId()).isNotNull();
    }
}
