package com.simleetag.homework.api.domain.home;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.api.dto.HomeModifyRequest;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.work.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Home extends DeletableEntity {

    @OneToMany(mappedBy = "home")
    private final List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "home")
    private final List<Category> categories = new ArrayList<>();

    @Column
    private String homeName;

    /* 유저가 집안일을 한 번이라도 초기화했는지에 대한 여부 */
    @Column
    private Boolean initialized = false;

    public Home(String homeName) {
        this.homeName = homeName;
    }

    public void addBy(Member member) {
        this.members.add(member);
        if (member.getHome() != this) {
            member.setBy(this);
        }
    }

    public void modify(HomeModifyRequest request) {
        this.homeName = request.homeName();
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }

    public void initialize() {
        this.initialized = true;
    }
}
