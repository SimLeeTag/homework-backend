package com.simleetag.homework.api.domain.user.api;

import java.util.List;

import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 계정")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/users")
public class UserMaintenanceController {
    private final UserService userService;

    @Operation(summary = "조회")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findOne(@PathVariable Long id) {
        final User user = userService.findById(id);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @Operation(summary = "생성")
    @PostMapping
    public ResponseEntity<Long> signUp(UserSignUpRequest request) {
        return ResponseEntity.ok(userService.add(request).getId());
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public ResponseEntity<Long> editProfile(@PathVariable Long id, UserProfileRequest request) {
        return ResponseEntity.ok(userService.editProfile(id, request).getId());
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> expire(@PathVariable Long id) {
        return ResponseEntity.ok(userService.expire(id).getId());
    }
}
