package com.simleetag.homework.api.domain.user.api;

import java.util.List;

import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserResponse;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 계정 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/users")
public class UserMaintenanceController {
    private final UserService userService;

    @Operation(summary = "검색")
    @GetMapping
    public ResponseEntity<List<UserResponse>> search(@RequestParam(required = false) Long id) {
        var condition = new UserSearchCondition(id);
        final List<User> users = userService.findAll(condition);
        return ResponseEntity.ok(UserResponse.from(users));
    }

    @Operation(summary = "생성")
    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody UserSignUpRequest request) {
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

    public record UserSearchCondition(
            Long userId
    ) {
        private static Specification<User> equalUserId(Long userId) {
            return (root, query, builder) -> builder.equal(root.get("id"), userId);
        }

        public Specification<User> toSpecs() {
            Specification<User> spec = (root, query, builder) -> null;
            if (userId != null)
                spec = spec.and(equalUserId(userId));
            return spec;
        }
    }
}
