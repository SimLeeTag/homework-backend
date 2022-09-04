package com.simleetag.homework.api.domain.user.oauth.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "Homework 서비스를 사용하기 위한 JWT")
        @NotBlank
        String homeworkToken,

        @Schema(description = "Homework에 가입된 유저의 정보")
        @NotBlank UserWithHomesResponse user
) {
    public static TokenResponse from(String homeworkToken, User user, List<Home> homes) {
        return new TokenResponse(homeworkToken, UserWithHomesResponse.from(user, homes));
    }
}
