package com.simleetag.homework.api.domain.home.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.simleetag.homework.api.domain.home.Home;

public class HomeResources {

    private HomeResources() {}

    public static Home aFixtureWithNoMembers(Long id) {
        return aFixtureWithNoMembers(id, "테스트 집");
    }

    public static Home aFixtureWithNoMembers(Long id, String name) {
        return Home.builder().id(id)
                   .createdAt(LocalDateTime.now())
                   .deletedAt(LocalDateTime.now())
                   .homeName(name)
                   .members(new ArrayList<>())
                   .build();
    }
}
