package com.simleetag.homework.api.domain.home.member;

import java.util.List;

import com.simleetag.homework.api.domain.home.member.repository.MemberDslRepository;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberFinder {

    private static final String NOT_FOUND_MESSAGE = "MemberID[%d]를 가진 멤버가 존재하지 않습니다.";
    private static final String NOT_FOUND_GENERAL_MESSAGE = "해당 멤버가 존재하지 않습니다.";

    private final MemberRepository memberRepository;

    private final MemberDslRepository memberDslRepository;

    public Member findMemberByIdAndHomeId(Long memberId, Long homeId) {
        return memberDslRepository.findMemberByIdAndHomeId(memberId, homeId)
                                  .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, memberId)));
    }

    public Member findByIdOrElseThrow(Long memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, memberId)));
    }

    public List<Member> findAllByHomeId(Long homeId) {
        return memberRepository.findAllByHomeId(homeId);
    }

    public Member findByHomeIdAndUserId(Long homeId, Long userId) {
        return memberRepository.findByHomeIdAndUserIdAndDeletedAtIsNull(homeId, userId)
                                  .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_GENERAL_MESSAGE)));
    }
}
