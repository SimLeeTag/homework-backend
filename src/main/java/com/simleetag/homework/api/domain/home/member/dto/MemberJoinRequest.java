package com.simleetag.homework.api.domain.home.member.dto;

import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberJoinRequest(
        @Schema(description = "유저 ID")
        @Positive
        Long userId
) {
}
