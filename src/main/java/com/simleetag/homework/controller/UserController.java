package com.simleetag.homework.controller;

import javax.servlet.http.HttpServletResponse;

import com.simleetag.homework.dto.AccessTokenRequest;
import com.simleetag.homework.service.OAuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final OAuthService oauthService;

    @GetMapping("/oauth")
    public ResponseEntity<String> login(final AccessTokenRequest accessTokenRequest, final HttpServletResponse response) {
        return ResponseEntity.ok(oauthService.signUpOrLogin(accessTokenRequest));
    }
}
