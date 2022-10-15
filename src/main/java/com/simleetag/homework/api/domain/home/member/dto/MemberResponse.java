package com.simleetag.homework.api.domain.home.member.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

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

        @Schema(description = "멤버가 가진 포인트")
        @PositiveOrZero
        Integer point,

        @Schema(description = "멤버 생성 시각")
        LocalDateTime createdAt,

        @Schema(description = "집 나간 시각")
        LocalDateTime deletedAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUserId(),
                member.getPoint(),
                member.getCreatedAt(),
                member.getDeletedAt()
        );
    }
}
