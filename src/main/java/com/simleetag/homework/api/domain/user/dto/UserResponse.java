package com.simleetag.homework.api.domain.user.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    /**
     * 유저 ID
     */
    @NotBlank
    private final Long id;

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

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage()
        );
    }
}
