package com.simleetag.homework.api.domain.home;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;
import com.simleetag.homework.api.domain.home.repository.HomeRepository;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HomeService {

    private final UserService userService;
    private final MemberService memberService;
    private final HomeRepository homeRepository;
    private final HomeJwt homeJwt;

    public List<HomeWithMembersResponse> findAllByMemberIds(List<Long> memberIds) {
        final List<Long> homeIds = memberService.findAllByIds(memberIds).stream().map(Member::getHome).map(Home::getId).toList();

        return findAllByIds(homeIds);
    }

    private List<HomeWithMembersResponse> findAllByIds(List<Long> homeIds) {
        return homeIds.stream().map(this::findById).toList();
    }

    public Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));
    }

    public HomeWithMembersResponse findById(Long homeId) {
        final Home home = findHomeById(homeId);
        final List<Member> members = home.getMembers();
        final List<User> users = members.stream().map(Member::getUserId).distinct().map(userService::findById).toList();

        return HomeWithMembersResponse.from(home, MemberResponse.from(members, users));
    }

    public CreatedHomeResponse createHome(Long userId, CreateHomeRequest homeRequest) {
        final Home home = homeRepository.save(new Home(homeRequest.homeName()));

        memberService.join(home, userId);

        return CreatedHomeResponse.from(home, homeJwt);
    }

    public MemberIdResponse joinHome(Long userId, Long homeId) {
        Optional<Member> joinedMember = memberService.findMemberByHomeIdAndUserId(homeId, userId);
        if (joinedMember.isPresent()) {
            return new MemberIdResponse(joinedMember.get().getId());
        }

        Home home = findHomeById(homeId);

        final Member member = memberService.join(home, userId);

        return new MemberIdResponse(member.getId());
    }
}
