package com.simleetag.homework.api.domain.user.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "유저 ID")
        @NotBlank
        Long userId,

        @Schema(description = "유저 이름")
        @NotBlank
        String userName,

        @Schema(description = "프로필 이미지 경로")
        @NotBlank
        String profileImage,

        @Schema(description = "OAuth 로그인에 사용한 Provider(ex. 카카오, 구글, ...)로부터 받은 ID")
        @NotBlank
        String oauthId,

        @Schema(description = "유저가 속한 집에서 유저에게 할당한 ID")
        @NotBlank
        List<Long> memberIds,

        @Schema(description = "유저 생성 시각")
        LocalDateTime createdAt,

        @Schema(description = "유저 삭제 시각")
        LocalDateTime deletedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getProfileImage(),
                user.getOauthId(),
                user.getMemberIds(),
                user.getCreatedAt(),
                user.getDeletedAt()
        );
    }
}
