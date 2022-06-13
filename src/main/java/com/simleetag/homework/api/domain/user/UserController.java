package com.simleetag.homework.api.domain.user;

import com.simleetag.homework.api.domain.user.dto.UserRequest;
import com.simleetag.homework.api.domain.user.dto.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * @title AccessToken을 가진 유저 조회
     */
    @PostMapping("/users/me")
    public ResponseEntity<UserResponse> findUserByAccessToken(@RequestBody UserRequest userRequest) {
        final User user = userService.findByAccessToken(userRequest.getAccessToken());
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
