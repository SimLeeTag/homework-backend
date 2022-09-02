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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "`user`")
@Entity
public class User extends DeletableEntity {
    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();
    @Column
    private String oauthId;
    @Column
    private String profileImage;
    @Column
    private String userName;

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
        this.userName = request.userName();
        this.profileImage = request.profileImage();
    }

    public void addBy(Member member) {
        this.members.add(member);
        if (member.getUser() != this) {
            member.setBy(this);
        }
    }
}
