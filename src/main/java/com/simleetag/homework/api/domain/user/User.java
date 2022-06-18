package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends DeletableEntity {

    @Column
    private String oauthId;

    @Column
    private String accessToken;

    @Column
    private String userName;

    @Column
    private String profileImage;

    @Column
    private Integer point;

    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();

    @Builder
    public User(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, String oauthId, String accessToken, String userName, String profileImage, Integer point, List<Member> members) {
        super(id, createdAt, deletedAt);
        this.oauthId = oauthId;
        this.accessToken = accessToken;
        this.userName = userName;
        this.profileImage = profileImage;
        this.point = point;
        this.members = members;
    }

    public User login(String oauthId, OAuthJwt oauthJwt) {
        this.oauthId = oauthId;
        this.accessToken = oauthJwt.createAccessToken(id);
        return this;
    }
}
