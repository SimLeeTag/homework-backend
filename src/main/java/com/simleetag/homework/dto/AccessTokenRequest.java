package com.simleetag.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenRequest {
    private String code;
    private String providerType;
}
