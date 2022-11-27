package com.simleetag.homework.api.domain.user.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record findUserWithHomeAndMembersResponse(
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
    public static findUserWithHomeAndMembersResponse from(User user) {
        final List<Home> userHomes = user.getMembers()
                                         .stream()
                                         .map(Member::getHome)
                                         .toList();

        return new findUserWithHomeAndMembersResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage(),
                HomeWithMembersResponse.from(userHomes)
        );
    }
}
