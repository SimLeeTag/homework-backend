package com.simleetag.homework.api.domain.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;
import com.simleetag.homework.api.domain.home.repository.HomeRepository;
import com.simleetag.homework.api.domain.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final MemberService memberService;
    private final HomeJwt homeJwt;

    public List<Home> findAllWithMembersByHomeIds(List<Long> homeIds) {
        return homeRepository.findAllWithMembersByIdIn(homeIds);
    }

    public CreatedHomeResponse createHome(CreateHomeRequest homeRequest, User user) {
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        // 집 저장
        Home home = new Home.HomeBuilder()
                .homeName(homeRequest.homeName())
                .members(new ArrayList<>())
                .build();

        homeRepository.save(home);

        // 집 입장
        Member member = Member.builder()
                              .point(0)
                              .build();

        member.setBy(user);
        member.setBy(home);

        memberService.save(member);

        return CreatedHomeResponse.from(home, homeJwt);
    }

    public HomeResponse findById(Long homeId) {
        return HomeResponse.from(findHomeById(homeId));
    }

    private Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));
    }

    public MemberIdResponse joinHome(Long homeId, User user) {
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        Optional<Member> joinedMember = memberService.findMemberByHomeIdAndUserId(homeId, user.getId());
        if (joinedMember.isPresent()) {
            return new MemberIdResponse(joinedMember.get().getId());
        }

        Home home = findHomeById(homeId);

        // 집 입장
        Member member = Member.builder()
                              .point(0)
                              .build();

        member.setBy(user);
        member.setBy(home);

        return new MemberIdResponse(memberService.save(member).getId());
    }
}
