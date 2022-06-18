package com.simleetag.homework.api.domain.member;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member extends DeletableEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Home home;

    @Builder
    public Member(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, User user, Home home) {
        super(id, createdAt, deletedAt);
        this.user = user;
        this.home = home;
    }
}
