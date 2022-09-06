package com.simleetag.homework.api.domain.user.api;

import java.util.List;

import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 계정")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final HomeService homeService;
    private final MemberService memberService;

    @Operation(
            summary = "유저 조회",
            description = "Homework 토큰으로 사용자 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserWithHomesResponse> findUserByAccessToken(@Login Long userId) {
        final User user = userService.findById(userId);
        final List<Long> homeIds = memberService.findAllHomeIdsByUserId(userId);
        final List<HomeWithMembersResponse> homes = homeIds.stream()
                                                           .map(homeService::findById)
                                                           .toList();

        return ResponseEntity.ok(UserWithHomesResponse.from(user, homes));
    }

    @Operation(
            summary = "유저 프로필 수정",
            description = "Homework 토큰으로 사용자 정보를 조회합니다."
    )
    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> editProfile(@Login Long userId, @RequestBody UserProfileRequest request) {
        final User user = userService.editProfile(userId, request);
        final UserProfileResponse response = UserProfileResponse.from(user);
        return ResponseEntity.ok(response);
    }
}
