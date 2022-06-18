package com.simleetag.homework.api.domain.member;

import java.time.LocalDateTime;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.User;

public class MemberResources {

    private MemberResources() {}

    public static Member aFixture(Long memberId, User user, Home home) {
        return Member.builder()
                     .id(memberId)
                     .createdAt(LocalDateTime.now())
                     .deletedAt(LocalDateTime.now())
                     .user(user)
                     .home(home)
                     .build();
    }
}
