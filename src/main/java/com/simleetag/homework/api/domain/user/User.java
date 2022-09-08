package com.simleetag.homework.api.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "`user`")
@Entity
public class User extends DeletableEntity {

    @Column
    private final boolean deleted = false;

    @Column
    private String oauthId;

    @Column
    private String profileImage;

    @ElementCollection
    private final List<Long> memberIds = new ArrayList<>();

    @Column
    private String userName;

    public User(String oauthId) {
        this.oauthId = oauthId;
    }

    public void editProfile(UserProfileRequest request) {
        this.userName = request.userName();
        this.profileImage = request.profileImage();
    }
}
