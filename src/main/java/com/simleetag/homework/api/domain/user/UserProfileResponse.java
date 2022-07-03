package com.simleetag.homework.api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProfileResponse {

    /**
     * 변경된 유저의 이름
     */
    private String userName;

    /**
     * 변경된 유저의 프로필 이미지 URL
     */
    private String profileImage;

    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(user.getUserName(), user.getProfileImage());
    }
}
