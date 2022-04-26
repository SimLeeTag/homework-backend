package com.simleetag.homework.controller;

import com.simleetag.homework.dto.TokenRequest;
import com.simleetag.homework.dto.TokenResponse;
import com.simleetag.homework.service.OAuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final OAuthService oauthService;

    /**
     * @title 회원가입 또는 로그인
     */
    @GetMapping("/oauth")
    public ResponseEntity<TokenResponse> login(@ModelAttribute final TokenRequest tokenRequest) {
        return ResponseEntity.ok(oauthService.signUpOrLogin(tokenRequest));
    }
}
