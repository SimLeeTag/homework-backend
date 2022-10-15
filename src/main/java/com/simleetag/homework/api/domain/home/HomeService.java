package com.simleetag.homework.api.domain.home;

import java.util.List;
import java.util.Objects;

import com.simleetag.homework.api.common.BaseEntity;
import com.simleetag.homework.api.domain.home.api.dto.*;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.member.dto.MemberWithUserResponse;
import com.simleetag.homework.api.domain.home.repository.HomeDslRepository;
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

    private final HomeDslRepository homeDslRepository;

    private final HomeJwt homeJwt;

    public List<HomeWithMembersResponse> findAllByMemberIds(List<Long> memberIds) {
        final List<Long> homeIds = memberService.findAllByIds(memberIds).stream().map(Member::getHome).map(Home::getId).toList();

        return findAllByIds(homeIds);
    }

    private List<HomeWithMembersResponse> findAllByIds(List<Long> homeIds) {
        return homeIds.stream().map(this::findValidHomeWithMembers).toList();
    }

    public Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));
    }

    public Home findValidHomeById(Long homeId) {
        final Home home = findHomeById(homeId);
        if (Objects.nonNull(home.getDeletedAt())) {
            throw new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집은 삭제된 집입니다.", homeId));
        }

        return home;
    }

    public Home findValidHomeWithMembersById(Long homeId) {
        final Home home = homeRepository.findHomeWithMembersById(homeId)
                                        .orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId)));

        if (Objects.nonNull(home.getDeletedAt())) {
            throw new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집은 삭제된 집입니다.", homeId));
        }

        return home;
    }

    public HomeWithMembersResponse findValidHomeWithMembers(Long homeId) {
        final Home home = homeDslRepository.findValidHomeAndMembers(homeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("HomeID[%d]에 해당하는 집은 유효하지 않은 집입니다.", homeId)));
        final List<Member> members = home.getMembers();
        final List<User> users = members.stream().map(Member::getUserId).distinct().map(userService::findById).toList();

        return HomeWithMembersResponse.from(home, MemberWithUserResponse.from(members, users));
    }

    public CreatedHomeResponse createHome(Long userId, HomeCreateRequest homeRequest) {
        final Home home = homeRepository.save(new Home(homeRequest.homeName()));

        memberService.join(home, userId);

        return CreatedHomeResponse.from(home, homeJwt);
    }

    public Home createEmptyHome(EmptyHomeCreateRequest homeRequest) {
        return homeRepository.save(new Home(homeRequest.homeName()));
    }

    public Home modify(Long id, HomeModifyRequest request) {
        final Home home = findHomeById(id);
        home.modify(request);
        return home;
    }

    public Home kickOutAll(Long id) {
        return kickOut(id, getMemberIds(id));
    }

    public Home kickOut(Long homeId, List<Long> memberIds) {
        final Home home = findHomeById(homeId);
        final List<Member> members = memberService.findAllByIds(memberIds);
        home.kickOutAll(members);
        return home;
    }

    public Home expire(Long id) {
        final Home home = findHomeById(id);
        home.expire();
        return home;
    }

    private List<Long> getMemberIds(Long homeId) {
        return findHomeById(homeId)
                .getMembers()
                .stream()
                .map(BaseEntity::getId)
                .toList();
    }
}
