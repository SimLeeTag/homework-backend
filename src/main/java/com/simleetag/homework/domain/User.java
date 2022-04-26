package com.simleetag.homework.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.simleetag.homework.domain.oauth.OAuthJwt;
import com.simleetag.homework.domain.oauth.OAuthProvider;
import com.simleetag.homework.dto.AccessTokenResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends DeletableEntity {

    @Column(nullable = false)
    private Long oauthId;

    @Column(nullable = false)
    private String accessToken;

    @Column
    private String userName;

    @Column
    private String profileImage;

    public User login(OAuthProvider oauthProvider, String code, OAuthJwt jwt) {
        final AccessTokenResponse accessTokenResponse = oauthProvider.requestAccessToken(code);
        this.oauthId = oauthProvider.requestUserInformation(accessTokenResponse).getId();
        this.accessToken = jwt.createAccessToken(oauthId);
        return this;
    }
}
