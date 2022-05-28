package com.simleetag.homework.api.domain.oauth.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.oauth.infra.provider.ProviderType;

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
    private ProviderType providerType;
}
