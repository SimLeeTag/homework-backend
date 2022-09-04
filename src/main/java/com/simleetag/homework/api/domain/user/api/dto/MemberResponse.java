package com.simleetag.homework.api.domain.user.api.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(
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
