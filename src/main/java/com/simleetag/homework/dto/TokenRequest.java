package com.simleetag.homework.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenRequest {

    /**
     * AccessToken을 발급받기 위한 코드
     */
    @NotBlank
    private String code;

    /**
     * Authorization Server 이름
     */
    @NotBlank
    private String providerType;
}
