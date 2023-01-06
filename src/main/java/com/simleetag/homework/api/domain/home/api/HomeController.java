package com.simleetag.homework.api.domain.home.api;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.HomeJwt;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/homes")
public class HomeController {
    private final HomeService homeService;

    private final MemberService memberService;

    private final HomeFinder homeFinder;

    private final HomeJwt homeJwt;

    @Operation(
            summary = "집 생성",
            description = "집을 생성한 사용자는 자동으로 집의 멤버로 추가됩니다."
    )
    @PostMapping
    public ResponseEntity<CreatedHomeResponse> createHome(@Login Long userId,
                                                          @RequestBody @Valid final HomeCreateRequest request) {
        Home home = homeService.createHome(userId, request);
        return ResponseEntity.ok(CreatedHomeResponse.from(home, homeJwt));
    }

    @Operation(
            summary = "집 멤버 조회",
            description = "초대 링크로 집에 속한 멤버들을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<HomeWithMembersResponse> findMembersByToken(@Login Long userId, @Invitation Long homeId) {
        final Home home = homeFinder.findHomeWithMembers(homeId);
        return ResponseEntity.ok(HomeWithMembersResponse.from(home));
    }

    @Operation(
            summary = "집 들어가기"
    )
    @PostMapping("/{homeId}/members")
    public ResponseEntity<MemberIdResponse> joinHome(@Login Long userId,
                                                     @PathVariable @Positive Long homeId) {
        final Home home = homeFinder.findHomeWithMembers(homeId);
        final Member member = memberService.join(home, userId);
        return ResponseEntity.ok(new MemberIdResponse(member.getId()));
    }

}
