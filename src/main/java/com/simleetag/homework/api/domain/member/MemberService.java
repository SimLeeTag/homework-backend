package com.simleetag.homework.api.domain.member;

import java.util.List;
import java.util.stream.Collectors;

import com.simleetag.homework.api.domain.home.Home;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Home> findAllHomeByUserId(Long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .map(Member::getHome)
                               .collect(Collectors.toUnmodifiableList());
    }
}
