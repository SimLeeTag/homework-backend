package com.simleetag.homework.api.domain.user.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeResponse {

    /**
     * 집 ID
     */
    @NotBlank
    private final Long homeId;

    /**
     * 집 이름
     */
    @NotBlank
    private final String homeName;

    /**
     * 집에 속한 유저 목록
     */
    @NotBlank
    private final List<MemberResponse> members;

    public static List<HomeResponse> from(List<Home> homes) {
        List<HomeResponse> homeResponses = new ArrayList<>();
        for (Home home : homes) {
            homeResponses.add(new HomeResponse(
                    home.getId(),
                    home.getHomeName(),
                    MemberResponse.from(home.getMembers())
            ));
        }

        return homeResponses;
    }
}
