package com.simleetag.homework.api.domain.home;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.common.Login;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.user.LoginUser;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.domain.home.dto.HomeResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @title 초대 링크로 집 멤버 조회
     */
    @GetMapping("/homes")
    public ResponseEntity<HomeResponse> findMembersByToken(@Login LoginUser loginUser, @Invitation Long homeId) {
        return ResponseEntity.ok(homeService.findById(homeId));
    }

    /**
     * @title 집 들어가기
     */
    @PostMapping("/homes/{homeId}")
    public ResponseEntity<MemberIdResponse> joinHome(@Login LoginUser loginUser,
                                                     @PathVariable @Positive Long homeId) {
        final User user = userService.findUserWithMembersByUserId(loginUser.getUserId());
        return ResponseEntity.ok(homeService.joinHome(homeId, user));
    }


}
