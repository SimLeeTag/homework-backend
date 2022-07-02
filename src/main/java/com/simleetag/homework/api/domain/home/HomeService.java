package com.simleetag.homework.api.domain.home;

import java.util.ArrayList;
import java.util.List;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.infra.HomeJwt;
import com.simleetag.homework.api.domain.member.MemberService;
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

}
