package com.simleetag.homework.api.domain.home;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.member.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Home extends DeletableEntity {

    @OneToMany(mappedBy = "home")
    private final List<Member> members = new ArrayList<>();

    @Column
    private final boolean deleted = false;

    @Column
    private String homeName;

    @ElementCollection
    private final List<Long> categoryIds = new ArrayList<>();

    public Home(String homeName) {
        this.homeName = homeName;
    }

    public void addBy(Member member) {
        this.members.add(member);
        if (member.getHome() != this) {
            member.setBy(this);
        }
    }
}
