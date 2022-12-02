package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "`user`")
@Entity
public class User extends DeletableEntity {

    @OneToMany(mappedBy = "user")
    private final List<Member> members = new ArrayList<>();

    @Column
    private String oauthId;

    @Column
    private String profileImage;

    @Column
    private String userName;

    public User(String oauthId) {
        this.oauthId = oauthId;
    }

    public User(String oauthId, String profileImage, String userName) {
        this.oauthId = oauthId;
        this.profileImage = profileImage;
        this.userName = userName;
    }

    public void editProfile(UserProfileRequest request) {
        this.userName = request.userName();
        this.profileImage = request.profileImage();
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }
}
