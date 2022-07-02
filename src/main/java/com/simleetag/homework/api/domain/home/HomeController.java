package com.simleetag.homework.api.domain.home;

import javax.validation.Valid;

import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.user.LoginUser;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final UserService userService;

    private final HomeService homeService;

    /**
     * @title 집 생성
     */
    @PostMapping("/homes")
    public ResponseEntity<CreatedHomeResponse> createHome(@Login LoginUser loginUser,
                                                          @RequestBody @Valid final CreateHomeRequest request) {
        final User user = userService.findUserWithMembersByUserId(loginUser.getUserId());

        CreatedHomeResponse response = homeService.createHome(request, user);
        return ResponseEntity.ok(response);
    }

}
