package com.simleetag.homework.api.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.utils.JsonMapper;

import org.springframework.util.StringUtils;

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

    @Column
    private String textOfMemberIds;

    @Column
    private String userName;

    public User(String oauthId) {
        this.oauthId = oauthId;
    }

    public void editProfile(UserProfileRequest request) {
        this.userName = request.userName();
        this.profileImage = request.profileImage();
    }

    public void addMemberId(Long memberId) {
        final List<Long> memberIds = getMemberIds();
        memberIds.add(memberId);
        setMemberIds(memberIds);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getMemberIds() {
        if (!StringUtils.hasText(textOfMemberIds)) {
            return new ArrayList<>();
        }

        return JsonMapper.readValue(textOfMemberIds, List.class);
    }

    public void setMemberIds(List<Long> memberIds) {
        this.textOfMemberIds = JsonMapper.writeValueAsString(memberIds);
    }
}
