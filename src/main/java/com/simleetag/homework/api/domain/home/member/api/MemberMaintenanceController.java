package com.simleetag.homework.api.domain.home.member.api;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.member.Member;
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
    private final HomeService homeService;
    private final MemberService memberService;

    @Operation(summary = "조회")
    @GetMapping("{memberId}")
    public ResponseEntity<MemberResponse> findOne(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberService.findMemberByIdAndHomeId(homeId, memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @Operation(
            summary = "생성",
            description = "멤버 생성은 집에 멤버를 추가하는 것과 같습니다."
    )
    @PostMapping
    public ResponseEntity<MemberResponse> join(@PathVariable Long homeId,
                                               @RequestParam Long userId) {
        final Home home = homeService.findValidHomeWithMembersById(homeId);
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
            summary = "삭제",
            description = "집에서 나가는 것은 멤버를 집에서 삭제하는 것이므로 멤버 삭제와 같습니다."
    )
    @DeleteMapping("{memberId}")
    public ResponseEntity<MemberResponse> expire(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberService.expire(homeId, memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}
