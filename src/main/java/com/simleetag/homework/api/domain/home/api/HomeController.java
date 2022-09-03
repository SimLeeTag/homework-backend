package com.simleetag.homework.api.domain.home.api;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HomeController {

    private final UserService userService;

    private final HomeService homeService;

    @Operation(
            summary = "집 생성",
            description = "집을 생성한 사용자는 자동으로 집의 멤버로 추가됩니다."
    )
    @PostMapping("/homes")
    public ResponseEntity<CreatedHomeResponse> createHome(@Login Long userId,
                                                          @RequestBody @Valid final CreateHomeRequest request) {
        final User user = userService.findUserWithMembersByUserId(userId);
        CreatedHomeResponse response = homeService.createHome(request, user);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "집 멤버 조회",
            description = "초대 링크로 집에 속한 멤버들을 조회합니다."
    )
    @GetMapping("/homes")
    public ResponseEntity<HomeResponse> findMembersByToken(@Login Long userId, @Invitation Long homeId) {
        return ResponseEntity.ok(homeService.findById(homeId));
    }

    @Operation(
            summary = "집 들어가기"
    )
    @PostMapping("/homes/{homeId}")
    public ResponseEntity<MemberIdResponse> joinHome(@Login Long userId,
                                                     @PathVariable @Positive Long homeId) {
        final User user = userService.findUserWithMembersByUserId(userId);
        return ResponseEntity.ok(homeService.joinHome(homeId, user));
    }


}
