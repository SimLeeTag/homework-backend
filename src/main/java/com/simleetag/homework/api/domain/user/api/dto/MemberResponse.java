package com.simleetag.homework.api.domain.user.api.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.member.Member;

public record MemberResponse(
        /**
         * 멤버 ID
         */
        @NotBlank
        Long memberId,

        /**
         * 유저 ID
         * <p></p>
         * <b>유저 ID vs 멤버 ID</b><br>
         * - 유저 ID : 집과 상관없이 가지고 있는 유저의 ID</p>
         * - 멤버 ID : 집의 ID와집에 소속된 유저 ID를 이용하여 만드는 멤버 식별자</p>
         */
        @NotBlank
        Long userId,

        /**
         * 유저 이름
         */
        @NotBlank
        String userName,

        /**
         * 프로필 이미지 경로
         */
        @NotBlank
        String profileImage,

        /**
         * 멤버가 가진 포인트
         * <br>
         * 유저는 집마다 다른 포인트를 가지고 있다.
         */
        @NotBlank
        Integer point
) {

    public static List<MemberResponse> from(List<Member> members) {
        List<MemberResponse> memberResponses = new ArrayList<>();
        for (Member member : members) {
            memberResponses.add(
                    new MemberResponse(
                            member.getId(),
                            member.getUser().getId(),
                            member.getUser().getUserName(),
                            member.getUser().getProfileImage(),
                            member.getPoint()
                    ));
        }

        return memberResponses;
    }
}
