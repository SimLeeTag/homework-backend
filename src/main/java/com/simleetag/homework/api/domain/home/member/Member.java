package com.simleetag.homework.api.domain.home.member;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
    @JoinColumn(name = "home_id")
    private Home home;
    @Column
    private Integer point;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Member(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, User user, Home home, Integer point) {
        super(id, createdAt, deletedAt);
        this.user = user;
        this.home = home;
        this.point = point;
    }

    public void setBy(User user) {
        this.user = user;
        if (!user.getMembers().contains(this)) {
            user.addBy(this);
        }
    }

    public void setBy(Home home) {
        this.home = home;
        if (!home.getMembers().contains(this)) {
            home.addBy(this);
        }
    }
}
