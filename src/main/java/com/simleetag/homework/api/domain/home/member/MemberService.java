package com.simleetag.homework.api.domain.home.member;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Long> findAllHomeIdsByUserId(Long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .map(Member::getHome)
                               .map(Home::getId)
                               .toList();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findMemberByHomeIdAndUserId(Long homeId, Long userId) {
        return memberRepository.findByHomeIdAndUserId(homeId, userId);
    }

    public List<Member> findAllById(List<Long> memberIds) {
        return memberRepository.findAllById(memberIds);
    }
}
