package com.simleetag.homework.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponse {

    /**
     * Homework 로그인에 필요한 AccessToken
     */
    @NotBlank
    private String accessToken;

    /**
     * AccessToken 재발급을 위한 RefreshToken
     */
    @NotBlank
    private String refreshToken;
}
