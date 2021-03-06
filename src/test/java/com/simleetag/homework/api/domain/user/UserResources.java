package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeResources;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.member.MemberResources;

public class UserResources {

    private UserResources() {}

    public static User aFixtureWithNoMembers(Long id) {
        return aFixtureWithNoMembers(id, "테스트");
    }

    public static User aFixtureWithNoMembers(Long id, String name) {
        return User.builder().id(id)
                   .createdAt(LocalDateTime.now())
                   .deletedAt(LocalDateTime.now())
                   .oauthId("12345")
                   .userName(name)
                   .profileImage("https://image.com/image.jpg")
                   .members(new ArrayList<>())
                   .build();
    }

    public static User aFixtureWithMembers(Long id) {
        final User user = aFixtureWithNoMembers(id);
        final Home home = HomeResources.aFixtureWithNoMembers(null);
        final Member member = MemberResources.aFixture(null, user, home);
        user.getMembers().add(member);
        return user;
    }
    public static User aFixtureWithMaximumMembers(Long id) {
        final User user = aFixtureWithNoMembers(id);
        final Member member1 = MemberResources.aFixture(null, user, null);
        final Member member2 = MemberResources.aFixture(null, user, null);
        final Member member3 = MemberResources.aFixture(null, user, null);
        user.getMembers().add(member1);
        user.getMembers().add(member2);
        user.getMembers().add(member3);
        return user;
    }
}
