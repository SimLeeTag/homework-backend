package com.simleetag.homework.api.domain.home.member.api;

import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.member.MemberService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "멤버")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final HomeFinder homeFinder;

}
