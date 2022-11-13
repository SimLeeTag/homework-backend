package com.simleetag.homework.api.domain.home.member.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberWithUserResponse(
        @Schema(description = "멤버 ID")
        @NotBlank
        Long memberId,

        @Schema(
                description = """
                        유저 ID
                                                
                        유저 ID vs 멤버 ID
                        - 유저 ID : 집과 상관없이 가지고 있는 유저의 ID</p>
                        - 멤버 ID : 집에 속해있는 유저들을 구분해내기 위한 식별자
                        """
        )
        @NotBlank
        Long userId,

        @Schema(description = "유저 이름")
        @NotBlank
        String userName,

        @Schema(description = "프로필 이미지 경로")
        @NotBlank
        String profileImage,

        @Schema(description = "멤버가 가진 포인트")
        @NotBlank
        Integer point
) {
    private static final String EXCEPTION_MESSAGE = "[%d] ID를 가진 멤버의 정보가 존재하지 않습니다.";

    public static List<MemberWithUserResponse> from(List<Member> members, List<User> users) {
        List<MemberWithUserResponse> memberWithUserResponse = new ArrayList<>();
        for (Member member : members) {
            final User matchingUser =
                    users.stream()
                         .filter(user -> member.getUserId().equals(user.getId()))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, member.getUserId())));

            memberWithUserResponse.add(
                    new MemberWithUserResponse(
                            member.getId(),
                            matchingUser.getId(),
                            matchingUser.getUserName(),
                            matchingUser.getProfileImage(),
                            member.getPoint()
                    ));
        }

        return memberWithUserResponse;
    }
}
