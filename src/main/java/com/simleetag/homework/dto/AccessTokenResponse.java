package com.simleetag.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    @Builder
    public AccessTokenResponse(String accessToken, String scope, String tokenType, String error, String errorDescription) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getAccessTokenAsBearer() {
        return String.format("%s %s", tokenType, accessToken);
    }
}
