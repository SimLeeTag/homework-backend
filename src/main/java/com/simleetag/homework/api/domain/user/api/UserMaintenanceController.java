package com.simleetag.homework.api.domain.user.api;

import com.simleetag.homework.api.domain.user.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 계정")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance")
public class UserMaintenanceController {
    private final UserService userService;

    @Operation(summary = "생성")
    @GetMapping
    public ResponseEntity<Long> signUp(UserSignUpRequest request) {
        return ResponseEntity.ok(userService.add(request).getId());
    }
}
