package com.simleetag.homework.api.domain.user.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserWithHomesResponse(
        @Schema(description = "유저 ID")
        @NotBlank
        Long userId,

        @Schema(description = "유저 이름")
        @NotBlank
        String userName,

        @Schema(description = "프로필 이미지 경로")
        @NotBlank
        String profileImage,

        @Schema(description = "유저가 속한 집 목록")
        @NotBlank
        List<HomeResponse> homes
) {
    public static UserWithHomesResponse from(User user, List<Home> homes) {
        return new UserWithHomesResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage(),
                HomeResponse.from(homes)
        );
    }
}
