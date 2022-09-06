package com.simleetag.homework.api.domain.home.member;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;
import com.simleetag.homework.api.domain.user.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final UserService userService;
    private final MemberRepository memberRepository;

    public List<Long> findAllHomeIdsByUserId(Long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .map(Member::getHome)
                               .map(Home::getId)
                               .toList();
    }

    public Optional<Member> findMemberByHomeIdAndUserId(Long homeId, Long userId) {
        return memberRepository.findByHomeIdAndUserId(homeId, userId);
    }

    public List<Member> findAllByIds(List<Long> memberIds) {
        return memberRepository.findAllById(memberIds);
    }

    public Member join(Home home, Long userId) {
        final List<Long> memberIds = userService.findById(userId).getMemberIds();
        if (memberIds.size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        final Member member = new Member(userId, 0);
        member.setBy(home);
        final Long memberId = memberRepository.save(member).getId();
        memberIds.add(memberId);

        return member;
    }
}
