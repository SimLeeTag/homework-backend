package com.simleetag.homework.api.domain.oauth.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.oauth.infra.provider.ProviderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenRequest {

    /**
     * OAuth Provider로 부터 발급받은 AccessToken
     */
    @NotBlank
    private String accessToken;

    /**
     * OAuth Provider 이름
     * <p>
     * Alert: ProviderType은 모두 [대문자]이어야 합니다
     */
    @NotBlank
    private ProviderType providerType;
}
