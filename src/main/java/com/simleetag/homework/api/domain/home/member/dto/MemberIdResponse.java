package com.simleetag.homework.api.domain.home.member.dto;

import javax.validation.constraints.NotBlank;

public record MemberIdResponse(
        /**
         * 멤버 ID
         */
        @NotBlank
        Long memberId
) {}
