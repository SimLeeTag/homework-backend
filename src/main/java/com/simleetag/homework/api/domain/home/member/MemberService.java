package com.simleetag.homework.api.domain.home.member;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.dto.MemberModifyRequest;
import com.simleetag.homework.api.domain.home.member.repository.MemberDslRepository;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;
import com.simleetag.homework.api.domain.user.User;
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

    private final MemberDslRepository memberDslRepository;

    public List<Long> findAllHomeIdsByUserId(Long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .map(Member::getHome)
                               .map(Home::getId)
                               .toList();
    }

    public Member findMemberByIdAndHomeId(Long memberId, Long homeId) {
        return memberDslRepository.findMemberByIdAndHomeId(memberId, homeId)
                                  .orElseThrow(() -> new IllegalArgumentException(
                                          String.format("MemberID[%d]를 가진 멤버가 존재하지 않습니다.", memberId)
                                  ));
    }

    public Member findValidMemberByIdAndHomeId(Long memberId, Long homeId) {
        return memberDslRepository.findValidMemberByIdAndHomeId(memberId, homeId)
                                  .orElseThrow(() -> new IllegalArgumentException(
                                          String.format("MemberID[%d]를 가진 유효한 멤버가 존재하지 않습니다.", memberId)
                                  ));
    }

    public List<Member> findAllByIds(List<Long> memberIds) {
        return memberRepository.findAllById(memberIds);
    }

    public Member join(Home home, Long userId) {
        final User user = userService.findById(userId);
        if (user.getMemberIds().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        final List<Member> members = home.getMembers();
        final Optional<Member> optionalMember = members.stream()
                                                       .filter(member -> member.getUserId().equals(user.getId()))
                                                       .findAny();
        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        final Member member = new Member(userId, 0);
        member.setBy(home);
        final Long memberId = memberRepository.save(member).getId();
        user.getMemberIds().add(memberId);
        return member;
    }

    public Member expire(Long homeId, Long memberId) {
        final Member member = findValidMemberByIdAndHomeId(memberId, homeId);
        member.expire();
        return member;
    }

    public Member modify(Long homeId, Long memberId, MemberModifyRequest request) {
        final Member member = findValidMemberByIdAndHomeId(memberId, homeId);
        member.modify(request);
        return member;
    }
}
