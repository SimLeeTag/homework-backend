package com.simleetag.homework.api.domain.user.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
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
        List<HomeWithMembersResponse> homes
) {
    public static UserWithHomesResponse from(User user, List<HomeWithMembersResponse> homeWithMembersResponses) {
        return new UserWithHomesResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage(),
                homeWithMembersResponses
        );
    }
}
