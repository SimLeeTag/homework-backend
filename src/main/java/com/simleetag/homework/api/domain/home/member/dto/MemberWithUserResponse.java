package com.simleetag.homework.api.domain.home.member.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.member.Member;

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
    public static List<MemberWithUserResponse> from(List<Member> members) {
        return members.stream()
                      .map(member -> new MemberWithUserResponse(
                              member.getId(),
                              member.getUser().getId(),
                              member.getUser().getUserName(),
                              member.getUser().getProfileImage(),
                              member.getPoint()))
                      .toList();
    }
}
