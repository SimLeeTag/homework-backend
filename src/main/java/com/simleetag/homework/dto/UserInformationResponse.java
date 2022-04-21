package com.simleetag.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInformationResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("app_id")
    private String appId;
}
