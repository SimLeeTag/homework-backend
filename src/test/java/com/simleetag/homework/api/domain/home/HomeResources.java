package com.simleetag.homework.api.domain.home;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.member.MemberResources;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserResources;

public class HomeResources {

    private HomeResources() {}

    public static Home aFixtureWithNoMembers() {
        return Home.builder().id(1L)
                   .createdAt(LocalDateTime.now())
                   .deletedAt(LocalDateTime.now())
                   .homeName("에버 하우스")
                   .members(new ArrayList<>())
                   .build();
    }

    public static Home aFixtureWithMembers() {
        final User user = UserResources.aFixtureWithNoMembers();
        final Home home = aFixtureWithNoMembers();
        final Member member = MemberResources.aFixture(1L, user, home);
        home.getMembers().add(member);
        return home;
    }
}
