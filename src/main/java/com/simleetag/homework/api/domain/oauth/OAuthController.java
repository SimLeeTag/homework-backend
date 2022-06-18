package com.simleetag.homework.api.domain.oauth;

import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * @title 회원가입 또는 로그인
     */
    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> login(final TokenRequest tokenRequest) {
        return ResponseEntity.ok(oauthService.signUpOrLogin(tokenRequest));
    }
}
