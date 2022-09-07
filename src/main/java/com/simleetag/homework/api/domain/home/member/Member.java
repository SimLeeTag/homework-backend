package com.simleetag.homework.api.domain.home.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.Home;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member extends DeletableEntity {

    @Column
    private final boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @Column
    private Integer point;

    @Column
    private Long userId;

    public Member(Long userId, Integer point) {
        this.userId = userId;
        this.point = point;
    }

    public void setBy(Home home) {
        this.home = home;
        if (!home.getMembers().contains(this)) {
            home.addBy(this);
        }
    }
}
