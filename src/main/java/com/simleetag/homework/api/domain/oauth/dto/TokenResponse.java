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
     * Homework 서비스를 사용하기 위한 JWT
     */
    @NotBlank
    private String homeworkToken;

    /**
     * Homework에 가입된 유저의 정보
     */
    @NotBlank
    private UserWithHomesResponse user;

    public static TokenResponse from(String homeworkToken, User user, List<Home> homes) {
        return new TokenResponse(homeworkToken, UserWithHomesResponse.from(user, homes));
    }
}
