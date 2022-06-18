package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.member.MemberResources;

public class UserResources {

    private UserResources() {}

    public static User aFixtureWithNoMembers() {
        return User.builder().id(1L)
                   .createdAt(LocalDateTime.now())
                   .deletedAt(LocalDateTime.now())
                   .oauthId("12345")
                   .accessToken("aaa.bbb.ccc")
                   .userName("Ever")
                   .profileImage("https://image.com/image.jpg")
                   .point(500)
                   .members(new ArrayList<>())
                   .build();
    }

    public static User aFixtureWithMembers() {
        final User user = aFixtureWithNoMembers();
        final Home home = HomeResources.aFixtureWithNoMembers();
        final Member member = MemberResources.aFixture(1L, user, home);
        user.getMembers().add(member);
        return user;
    }
}
