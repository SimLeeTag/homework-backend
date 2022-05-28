package com.simleetag.homework.api.domain.oauth.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponse {

    /**
     * Homework 로그인에 필요한 AccessToken.
     */
    @NotBlank
    private String accessToken;
}
