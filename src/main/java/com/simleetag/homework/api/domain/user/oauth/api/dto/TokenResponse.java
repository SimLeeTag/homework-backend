package com.simleetag.homework.api.domain.user.oauth.api.dto;

import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomeAndMembersResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public record TokenResponse(
        @Schema(description = "Homework 서비스를 사용하기 위한 JWT")
        @NotBlank
        String homeworkToken,

        @Schema(description = "Homework에 가입된 유저의 정보")
        @NotBlank UserWithHomeAndMembersResponse user
) {
    public static TokenResponse from(String homeworkToken, UserWithHomeAndMembersResponse user) {
        return new TokenResponse(homeworkToken, user);
    }

    public static TokenResponse from(String homeworkToken, User user) {
        return from(homeworkToken, UserWithHomeAndMembersResponse.from(user));
    }
}
