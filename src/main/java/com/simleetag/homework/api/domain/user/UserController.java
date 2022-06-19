package com.simleetag.homework.api.domain.user;

import java.util.List;

import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.user.dto.UserWithHomesResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final HomeService homeService;

    /**
     * @title AccessToken으로 유저 조회
     */
    @GetMapping("/users/me")
    public ResponseEntity<UserWithHomesResponse> findUserByAccessToken(@Login LoginUser logInUser) {
        final User user = userService.findById(logInUser.getUserId());
        final List<Home> homes = homeService.findAllWithMembers(logInUser.getUserId());
        return ResponseEntity.ok(UserWithHomesResponse.from(user, homes));
    }
}
