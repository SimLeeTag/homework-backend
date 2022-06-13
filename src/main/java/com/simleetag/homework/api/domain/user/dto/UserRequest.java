package com.simleetag.homework.api.domain.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {

    /**
     * OAuth 로그인 시 Homework 서비스로부터 발급받은 토큰
     */
    @NotBlank
    private String accessToken;
}
