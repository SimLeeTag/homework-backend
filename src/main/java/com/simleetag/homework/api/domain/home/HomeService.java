package com.simleetag.homework.api.domain.home;

import java.util.ArrayList;
import java.util.List;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.infra.HomeJwt;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.member.MemberService;
import com.simleetag.homework.api.domain.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HomeService {

    private final HomeRepository homeRepository;
    private final MemberService memberService;

    private final HomeJwt homeJwt;

    public List<Home> findAllWithMembers(Long userId) {
        final List<Long> homeIds = memberService.findAllHomeIdsByUserId(userId);
        return homeRepository.findAllWithMembersByIdIn(homeIds);
    }

    @Transactional
    public CreatedHomeResponse createHome(CreateHomeRequest homeRequest, User user) {
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }
        Home newHome = createNewHome(homeRequest, user);
        homeRepository.save(newHome);
        return CreatedHomeResponse.from(newHome, homeJwt);
    }

    private Home createNewHome(CreateHomeRequest homeRequest, User user) {
        Home newHome = new Home.HomeBuilder()
                .homeName(homeRequest.getHomeName())
                .members(new ArrayList<>())
                .build();
        newHome.addMember(user);
        return newHome;
    }

    public HomeResponse findById(Long homeId) {
        return HomeResponse.from(findHomeById(homeId));
    }

    private Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));
    }

    @Transactional
    public MemberIdResponse joinHome(Long homeId, User user) {
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        Home home = findHomeById(homeId);

        Member member = Member.builder()
                              .user(user)
                              .home(home)
                              .point(0)
                              .build();

        Member newMember = memberService.save(member);

        return new MemberIdResponse(newMember.getId());
    }
}
