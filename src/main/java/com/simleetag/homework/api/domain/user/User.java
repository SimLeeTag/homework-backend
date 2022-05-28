package com.simleetag.homework.api.domain.user;

import java.io.IOException;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.oauth.dto.AccessTokenResponse;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.oauth.infra.provider.OAuthProvider;

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

    public User login(OAuthProvider oauthProvider, String code, OAuthJwt jwt) throws IOException {
        final AccessTokenResponse accessTokenResponse = oauthProvider.requestAccessToken(code);
        this.oauthId = oauthProvider.requestUserInformation(accessTokenResponse).getId();
        this.accessToken = jwt.createAccessToken(oauthId);
        return this;
    }
}
