package com.simleetag.homework.api.domain.member.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberIdResponse {
    /**
     * 멤버 ID
     */
    @NotBlank
    private final Long memberId;

}
