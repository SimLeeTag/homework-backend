package com.simleetag.homework.api.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.simleetag.homework.api.common.DeletableEntity;
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

    public User login(OAuthProvider oauthProvider, String accessToken, OAuthJwt jwt) {
        this.oauthId = oauthProvider.requestUserInformation(accessToken).getId();
        this.accessToken = jwt.createAccessToken(oauthId);
        return this;
    }
}
