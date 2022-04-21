package com.simleetag.homework.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.simleetag.homework.domain.oauth.OAuthProvider;
import com.simleetag.homework.dto.AccessTokenResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends DeletableEntity {

    @Column(nullable = false)
    private Long oauthId;

    @Column
    private String userName;

    @Column
    private String profileImage;

    @Builder
    public User(Long oauthId, String userName, String profileImage) {
        this.oauthId = oauthId;
        this.userName = userName;
        this.profileImage = profileImage;
    }

    public User login(OAuthProvider oauthProvider, String code) {
        final AccessTokenResponse accessTokenResponse = oauthProvider.requestAccessToken(code);
        this.oauthId = oauthProvider.requestUserInformation(accessTokenResponse).getId();
        return this;
    }
}
