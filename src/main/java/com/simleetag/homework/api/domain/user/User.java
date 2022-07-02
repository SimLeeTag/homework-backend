package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.user.dto.UserProfileRequest;

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
    private String userName;

    @Column
    private String profileImage;

    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();

    public User(String oauthId) {
        this.oauthId = oauthId;
    }

    @Builder
    public User(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, String oauthId, String userName, String profileImage, List<Member> members) {
        super(id, createdAt, deletedAt);
        this.oauthId = oauthId;
        this.userName = userName;
        this.profileImage = profileImage;
        this.members = members;
    }

    public void editProfile(UserProfileRequest request) {
        this.userName = request.getUserName();
        this.profileImage = request.getProfileImage();
    }
}
