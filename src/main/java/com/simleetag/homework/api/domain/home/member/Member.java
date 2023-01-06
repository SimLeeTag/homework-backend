package com.simleetag.homework.api.domain.home.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.dto.MemberModifyRequest;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends DeletableEntity {

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @Column
    private Integer point;

    @OneToMany(mappedBy = "owner")
    private final List<TaskGroup> taskGroups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setBy(Home home) {
        this.home = home;
        if (!home.getMembers().contains(this)) {
            home.addBy(this);
        }
    }

    public void addBy(TaskGroup taskGroup) {
        this.taskGroups.add(taskGroup);
        if (taskGroup.getOwner() != this) {
            taskGroup.setOwnerBy(this);
        }
    }


    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }

    public void modify(MemberModifyRequest request) {
        this.point = request.point();
    }
}
