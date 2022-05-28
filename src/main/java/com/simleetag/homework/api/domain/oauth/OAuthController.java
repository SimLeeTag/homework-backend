package com.simleetag.homework.api.domain.oauth;

import java.io.IOException;

import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * @title 회원가입 또는 로그인
     */
    @GetMapping("/oauth")
    public ResponseEntity<TokenResponse> login(@ModelAttribute final TokenRequest tokenRequest) throws IOException {
        return ResponseEntity.ok(oauthService.signUpOrLogin(tokenRequest));
    }
}
