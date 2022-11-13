package com.simleetag.homework.api.domain.home.member.api;

import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "멤버")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/homes")
public class MemberController {
    private final HomeService homeService;

    private final MemberService memberService;

    @Operation(
            summary = "집 들어가기"
    )
    @PostMapping("/{homeId}")
    public ResponseEntity<MemberIdResponse> joinHome(@Login Long userId,
                                                     @PathVariable @Positive Long homeId) {
        final Home home = homeService.findValidHomeWithMembersById(homeId);
        final Member member = memberService.join(home, userId);
        return ResponseEntity.ok(new MemberIdResponse(member.getId()));
    }
}
