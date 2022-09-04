package com.simleetag.homework.api.domain.home;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.simleetag.homework.api.common.exception.HomeJoinException;
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
        final List<Long> homeIds = memberService.findAllById(memberIds)
                                                .stream()
                                                .map(Member::getHome)
                                                .map(Home::getId)
                                                .toList();

        return findAllByIdIn(homeIds);
    }

    public CreatedHomeResponse createHome(Long userId, CreateHomeRequest homeRequest) {
        final List<Long> memberIds = userService.findById(userId).getMemberIds();
        if (memberIds.size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        // 집 저장
        final Home home = homeRepository.save(new Home(homeRequest.homeName()));

        // 집 입장
        Member member = new Member(userId, 0);
        member.setBy(home);
        final Long memberId = memberService.save(member).getId();
        memberIds.add(memberId);

        return CreatedHomeResponse.from(home, homeJwt);
    }

    public HomeWithMembersResponse findById(Long homeId) {
        final Home home = findHomeById(homeId);
        final List<Member> members = home.getMembers();
        final Set<Long> userIds = members.stream()
                                         .map(Member::getUserId)
                                         .collect(Collectors.toSet());

        final List<User> users = userIds.stream()
                                        .map(userService::findById)
                                        .toList();

        return HomeWithMembersResponse.from(home, MemberResponse.from(members, users));
    }

    public List<HomeWithMembersResponse> findAllByIdIn(List<Long> homeIds) {
        return homeIds.stream().map(this::findById).toList();
    }

    private Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));
    }

    public MemberIdResponse joinHome(Long userId, Long homeId) {
        Optional<Member> joinedMember = memberService.findMemberByHomeIdAndUserId(homeId, userId);
        if (joinedMember.isPresent()) {
            return new MemberIdResponse(joinedMember.get().getId());
        }

        final List<Long> memberIds = userService.findById(userId).getMemberIds();
        if (memberIds.size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        Home home = findHomeById(homeId);

        // 집 입장
        Member member = new Member(userId, 0);
        member.setBy(home);
        final Long memberId = memberService.save(member).getId();
        memberIds.add(memberId);

        return new MemberIdResponse(memberService.save(member).getId());
    }
}
