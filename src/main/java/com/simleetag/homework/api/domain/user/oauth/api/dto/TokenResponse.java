package com.simleetag.homework.api.domain.user.oauth.api.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.api.dto.findUserWithHomeAndMembersResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "Homework 서비스를 사용하기 위한 JWT")
        @NotBlank
        String homeworkToken,

        @Schema(description = "Homework에 가입된 유저의 정보")
        @NotBlank findUserWithHomeAndMembersResponse user
) {
    public static TokenResponse from(String homeworkToken, findUserWithHomeAndMembersResponse user) {
        return new TokenResponse(homeworkToken, user);
    }

    public static TokenResponse from(String homeworkToken, User user) {
        return from(homeworkToken, findUserWithHomeAndMembersResponse.from(user));
    }
}
