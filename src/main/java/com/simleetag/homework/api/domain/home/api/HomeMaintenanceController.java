package com.simleetag.homework.api.domain.home.api;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.EmptyHomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeModifyRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "집 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/homes")
public class HomeMaintenanceController {
    private final HomeService homeService;

    private final MemberService memberService;

    private final HomeFinder homeFinder;

    @Operation(summary = "조회")
    @GetMapping("{homeId}")
    public ResponseEntity<HomeResponse> findOne(@PathVariable Long homeId) {
        final Home home = homeFinder.findHomeById(homeId);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "생성")
    @PostMapping
    public ResponseEntity<HomeResponse> create(@RequestBody EmptyHomeCreateRequest request) {
        final Home home = homeService.createEmptyHome(request);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "수정")
    @PutMapping("{homeId}")
    public ResponseEntity<HomeResponse> modify(@PathVariable Long homeId, HomeModifyRequest request) {
        final Home home = homeService.modify(homeId, request);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("{homeId}")
    public ResponseEntity<HomeResponse> expire(@PathVariable Long homeId) {
        final Home home = homeService.expire(homeId);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(
            summary = "멤버 생성",
            description = "멤버 생성은 집에 멤버를 추가하는 것과 같습니다."
    )
    @PostMapping("/{homeId}/members")
    public ResponseEntity<MemberResponse> join(@PathVariable Long homeId,
                                               @RequestParam Long userId) {
        final Home home = homeFinder.findHomeById(homeId);
        final Member member = memberService.join(home, userId);
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
            summary = "멤버 삭제",
            description = "멤버 삭제는 집에서 나가는 것과 같습니다."
    )
    @DeleteMapping("/{homeId}/members/{memberId}")
    public ResponseEntity<MemberResponse> expire(@PathVariable Long homeId, @PathVariable Long memberId) {
        final Member member = memberService.expire(homeId, memberId);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

}
