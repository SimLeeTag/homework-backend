package com.simleetag.homework.api.domain.user.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserWithHomesResponse {

    /**
     * 유저 ID
     */
    @NotBlank
    private final Long userId;

    /**
     * 유저 이름
     */
    @NotBlank
    private final String userName;

    /**
     * 프로필 이미지 경로
     */
    @NotBlank
    private final String profileImage;

    /**
     * 유저가 속한 집 목록
     */
    @NotBlank
    private final List<HomeResponse> homes;

    public static UserWithHomesResponse from(User user, List<Home> homes) {
        return new UserWithHomesResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage(),
                HomeResponse.from(homes)
        );
    }
}
