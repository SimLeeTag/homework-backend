package com.simleetag.homework.api.domain.home;

import com.simleetag.homework.api.domain.home.api.dto.EmptyHomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeModifyRequest;
import com.simleetag.homework.api.domain.home.member.MemberService;
import com.simleetag.homework.api.domain.home.repository.HomeRepository;
import com.simleetag.homework.api.domain.work.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HomeService {

    private final CategoryService categoryService;

    private final MemberService memberService;

    private final HomeRepository homeRepository;

    private final HomeFinder homeFinder;

    public Home createHome(Long userId, HomeCreateRequest homeRequest) {
        final Home home = homeRepository.save(new Home(homeRequest.homeName()));

        categoryService.setDefaultCategoriesAtHome(home);
        memberService.join(home, userId);

        return home;
    }

    public Home createEmptyHome(EmptyHomeCreateRequest homeRequest) {
        return homeRepository.save(new Home(homeRequest.homeName()));
    }

    public Home modify(Long id, HomeModifyRequest request) {
        final Home home = homeFinder.findHomeById(id);
        home.modify(request);
        return home;
    }

    public Home expire(Long id) {
        final Home home = homeFinder.findHomeById(id);
        home.expire();
        return home;
    }
}
