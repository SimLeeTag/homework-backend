package com.simleetag.homework.api.domain.user.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.user.User;

public record UserWithHomesResponse(
        /**
         * 유저 ID
         */
        @NotBlank
        Long userId,

        /**
         * 유저 이름
         */
        @NotBlank
        String userName,

        /**
         * 프로필 이미지 경로
         */
        @NotBlank
        String profileImage,

        /**
         * 유저가 속한 집 목록
         */
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
