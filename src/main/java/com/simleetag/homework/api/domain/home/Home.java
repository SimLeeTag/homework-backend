package com.simleetag.homework.api.domain.home;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.member.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Home extends DeletableEntity {

    @Column
    private String homeName;

    @OneToMany
    private List<Member> members = new ArrayList<>();

    @Builder
    public Home(Long id, LocalDateTime createdAt, LocalDateTime deletedAt, String homeName, List<Member> members) {
        super(id, createdAt, deletedAt);
        this.homeName = homeName;
        this.members = members;
    }
}
