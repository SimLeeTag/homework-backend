package com.simleetag.homework.api.domain.oauth.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.dto.UserWithHomesResponse;

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
     * Homework에 가입된 유저의 정보
     */
    @NotBlank
    private UserWithHomesResponse user;

    public static TokenResponse from(String accessToken, User user, List<Home> homes) {
        return new TokenResponse(accessToken, UserWithHomesResponse.from(user, homes));
    }
}
