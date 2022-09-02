package com.simleetag.homework.api.domain.user.api.dto;

import javax.validation.constraints.NotBlank;

public record UserProfileRequest(
        /**
         * 사용할 유저의 이름
         */
        @NotBlank
        String userName,

        /**
         * 사용할 유저의 프로필 이미지 URL
         */
        String profileImage
) {}
