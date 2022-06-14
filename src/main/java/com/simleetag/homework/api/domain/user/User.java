package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends DeletableEntity {

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    private String accessToken;

    @Column
    private String userName;

    @Column
    private String profileImage;

    public User(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, String oauthId, String accessToken, String userName, String profileImage) {
        super(id, createdAt, deletedAt);
        this.oauthId = oauthId;
        this.accessToken = accessToken;
        this.userName = userName;
        this.profileImage = profileImage;
    }

    public User login(String oauthId, OAuthJwt oauthJwt) {
        this.oauthId = oauthId;
        this.accessToken = oauthJwt.createAccessToken(id);
        return this;
    }
}
