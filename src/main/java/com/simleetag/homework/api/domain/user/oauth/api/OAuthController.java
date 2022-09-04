package com.simleetag.homework.api.domain.user.oauth.api;

import com.simleetag.homework.api.domain.user.oauth.OAuthService;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "회원가입 또는 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OAuthController {

    private final OAuthService oauthService;

    @Operation(
            summary = "회원가입 또는 로그인",
            description = "비회원일 경우 회원 가입, 회원일 경우 로그인 합니다."
    )
    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest) {
        return ResponseEntity.ok(oauthService.signUpOrLogin(tokenRequest));
    }
}
