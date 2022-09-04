package com.simleetag.homework.api.domain.user.api.dto;

import com.simleetag.homework.api.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileResponse(
        @Schema(description = "변경된 유저의 이름")
        String userName,

        @Schema(description = "변경된 유저의 프로필 이미지 URL")
        String profileImage
) {


    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(user.getUserName(), user.getProfileImage());
    }
}
