package com.simleetag.homework.api.domain.home;

import java.util.List;

import com.simleetag.homework.api.domain.member.MemberService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HomeService {

    private final MemberService memberService;

    public List<Home> findAllWithMembers(Long userId) {
        return memberService.findAllHomeByUserId(userId);
    }
}
