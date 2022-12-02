package com.simleetag.homework.api.domain.home;

import com.simleetag.homework.api.domain.home.repository.HomeDslRepository;
import com.simleetag.homework.api.domain.home.repository.HomeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HomeFinder {

    private static final String NOT_FOUND_MESSAGE = "HomeID[%d]에 해당하는 집이 존재하지 않습니다.";

    private final HomeRepository homeRepository;

    private final HomeDslRepository homeDslRepository;

    public Home findHomeById(Long homeId) {
        return homeRepository.findById(homeId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, homeId)));
    }

    public Home findHomeWithMembers(Long homeId) {
        return homeDslRepository.findHomeWithMembers(homeId)
                                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, homeId)));
    }
}
