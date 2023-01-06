package com.simleetag.homework.api.domain.home.member.api;

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
@RequestMapping("/maintenance/members")
public class MemberMaintenanceController {
    private final MemberService memberService;

    private final MemberFinder memberFinder;


    @Operation(summary = "조회")
    @GetMapping("{memberId}")
    public ResponseEntity<MemberResponse> findOne(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberFinder.findMemberByIdAndHomeId(homeId, memberId);
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

}
