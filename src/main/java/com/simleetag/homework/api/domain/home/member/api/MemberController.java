package com.simleetag.homework.api.domain.home.member.api;

import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberFinder;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "멤버")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberFinder memberFinder;

    @Operation(
            summary = "유저의 멤버 id 조회",
            description = """
                    해당 유저의 해당 집의 멤버 id를 조회합니다.
                    """
    )
    @GetMapping
    public ResponseEntity<MemberIdResponse> findMemberId(@Login Long userId, @RequestParam Long homeId) {
        final Member member = memberFinder.findByHomeIdAndUserId(homeId, userId);
        return ResponseEntity.ok(new MemberIdResponse(member.getId()));
    }

}
