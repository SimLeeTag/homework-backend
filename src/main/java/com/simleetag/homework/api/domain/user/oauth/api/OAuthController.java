package com.simleetag.homework.api.domain.user.oauth.api;

import com.simleetag.homework.api.domain.user.oauth.OAuthService;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest) {
        return ResponseEntity.ok(oauthService.signUpOrLogin(tokenRequest));
    }
}
