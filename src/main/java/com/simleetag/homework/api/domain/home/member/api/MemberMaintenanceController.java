package com.simleetag.homework.api.domain.home.member.api;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberFinder;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberModifyRequest;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "멤버 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/homes/{homeId}/members")
public class MemberMaintenanceController {
    private final MemberService memberService;

    private final MemberFinder memberFinder;

    private final HomeFinder homeFinder;

    @Operation(summary = "조회")
    @GetMapping("{memberId}")
    public ResponseEntity<MemberResponse> findOne(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberFinder.findMemberByIdAndHomeId(homeId, memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @Operation(
            summary = "생성",
            description = "멤버 생성은 집에 멤버를 추가하는 것과 같습니다."
    )
    @PostMapping
    public ResponseEntity<MemberResponse> join(@PathVariable Long homeId,
                                               @RequestParam Long userId) {
        final Home home = homeFinder.findHomeWithMembers(homeId);
        final Member member = memberService.join(home, userId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @Operation(summary = "수정")
    @PutMapping("{memberId}")
    public ResponseEntity<MemberResponse> modify(@PathVariable Long homeId,
                                                 @PathVariable Long memberId,
                                                 @RequestBody MemberModifyRequest request) {
        final Member member = memberService.modify(homeId, memberId, request);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @Operation(
            summary = "집의 모든 멤버 삭제",
            description = "집에서 모든 멤버를 삭제합니다."
    )
    @DeleteMapping
    public ResponseEntity<MemberResponse> expireAll(@PathVariable Long homeId) {
        memberService.expireAll(homeId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "삭제",
            description = "멤버 삭제는 집에서 나가는 것과 같습니다."
    )
    @DeleteMapping("{memberId}")
    public ResponseEntity<MemberResponse> expire(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberService.expire(homeId, memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}
