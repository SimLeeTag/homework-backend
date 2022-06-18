package com.simleetag.homework.api.domain.home;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
}
