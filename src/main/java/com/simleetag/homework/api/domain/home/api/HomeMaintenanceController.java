package com.simleetag.homework.api.domain.home.api;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.EmptyHomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeModifyRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/homes")
public class HomeMaintenanceController {
    private final HomeService homeService;

    private final HomeFinder homeFinder;

    @Operation(summary = "조회")
    @GetMapping("{homeId}")
    public ResponseEntity<HomeResponse> findOne(@PathVariable Long homeId) {
        final Home home = homeFinder.findHomeById(homeId);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "생성")
    @PostMapping
    public ResponseEntity<HomeResponse> create(EmptyHomeCreateRequest request) {
        final Home home = homeService.createEmptyHome(request);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "수정")
    @PutMapping("{homeId}")
    public ResponseEntity<HomeResponse> modify(@PathVariable Long homeId, HomeModifyRequest request) {
        final Home home = homeService.modify(homeId, request);
        return ResponseEntity.ok(HomeResponse.from(home));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("{homeId}")
    public ResponseEntity<HomeResponse> expire(@PathVariable Long homeId) {
        final Home home = homeService.expire(homeId);
        return ResponseEntity.ok(HomeResponse.from(home));
    }
}