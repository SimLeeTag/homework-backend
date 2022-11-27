package com.simleetag.homework.api.domain.home.member;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.dto.MemberModifyRequest;
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

    private final MemberFinder memberFinder;

    public Member join(Home home, Long userId) {
        final User user = userService.findById(userId);
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        return home.getMembers()
                   .stream()
                   .filter(member -> member.getUser().getId().equals(userId))
                   .findAny()
                   .orElseGet(() -> memberRepository.save(new Member(home, 0, null, user)));
    }

    public Member modify(Long homeId, Long memberId, MemberModifyRequest request) {
        final Member member = memberFinder.findMemberByIdAndHomeId(memberId, homeId);
        member.modify(request);
        return member;
    }

    public void expireAll(Long homeId) {
        memberFinder.findAllByHomeId(homeId).forEach(Member::expire);
    }

    public Member expire(Long homeId, Long memberId) {
        final Member member = memberFinder.findMemberByIdAndHomeId(memberId, homeId);
        member.expire();
        return member;
    }
}
