package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@Table(name = "`user`")
@Entity
public class User extends DeletableEntity {

    @Column
    private String oauthId;

    @Column
    private String profileImage;

    @Column
    private String textOfMemberIds;

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

    public List<Long> getMemberIds() {
        if (StringUtils.isBlank(textOfMemberIds)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                Arrays.stream(textOfMemberIds.split(","))
                      .map(Long::valueOf)
                      .toList()
        );
    }

    public void setMemberIds(List<Long> memberIds) {
        this.textOfMemberIds = StringUtils.join(memberIds, ",");
    }

    public void addMemberIds(Long id) {
        final List<Long> memberIds = getMemberIds();
        memberIds.add(id);
        setMemberIds(memberIds);
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }
}
