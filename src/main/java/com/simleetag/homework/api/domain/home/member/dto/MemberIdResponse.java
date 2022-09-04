package com.simleetag.homework.api.domain.home.member.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberIdResponse(
        @Schema(description = "멤버 ID")
        @NotBlank
        Long memberId
) {}
