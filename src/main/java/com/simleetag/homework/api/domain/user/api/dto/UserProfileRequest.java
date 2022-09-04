package com.simleetag.homework.api.domain.user.api.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileRequest(

        @Schema(description = "사용할 유저의 이름")
        @NotBlank
        String userName,

        @Schema(description = "사용할 유저의 프로필 이미지 URL")
        String profileImage
) {}
