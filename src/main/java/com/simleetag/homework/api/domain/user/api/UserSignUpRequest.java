package com.simleetag.homework.api.domain.user.api;

public record UserSignUpRequest(
        String oauthId,
        String profileImage,
        String userName
) {
}
