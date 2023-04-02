package com.simleetag.homework.api.domain.home.member.api;

import java.time.LocalDate;
import java.util.List;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberFinder;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskRateResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "멤버")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberFinder memberFinder;

    private final MemberService memberService;

    private final HomeFinder homeFinder;

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

    @Operation(
            summary = "집 들어가기",
            description = """
                    집에 들어간 유저는 해당 집의 멤버로 생성됩니다.
                    """
    )
    @PostMapping
    public ResponseEntity<MemberIdResponse> joinHome(@Login Long userId,
                                                     @Invitation Long homeId) {
        final Home home = homeFinder.findHomeWithMembers(homeId);
        final Member member = memberService.join(home, userId);
        return ResponseEntity.ok(new MemberIdResponse(member.getId()));
    }

    @Operation(
            summary = "날짜별, 멤버별 집안일 완료율 조회",
            description = """
                    해당 멤버의 해당 날짜의 집안일 완료율을 조회합니다.
                    """
    )
    @GetMapping("/{memberId}/tasks/rate")
    public ResponseEntity<List<TaskRateResponse>> checkRatesWithDueDate(@Invitation Long homeId,
                                                                        @PathVariable Long memberId,
                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(memberService.calculateTaskRatesByDueDates(memberId, startDate, endDate));
    }

    @Operation(
            summary = "날짜별, 멤버별 집안일 조회",
            description = """
                    해당 멤버의 해당 날짜의 등록된 집안일을 조회합니다.
                    """
    )
    @GetMapping("/{memberId}/tasks")
    public ResponseEntity<List<TaskResponse>> findAllWithDueDate(@Invitation Long homeId,
                                                                 @PathVariable Long memberId,
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(memberService.findAllTasksByDueDate(memberId, date));
    }
}
