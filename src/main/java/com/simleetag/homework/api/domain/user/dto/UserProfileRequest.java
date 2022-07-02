package com.simleetag.homework.api.domain.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProfileRequest {

    /**
     * 사용할 유저의 이름
     */
    @NotBlank
    private String userName;

    /**
     * 사용할 유저의 프로필 이미지 URL
     */
    @NotBlank
    private String profileImage;
}
