package com.simleetag.homework.utils;

import java.util.Arrays;

import com.simleetag.homework.api.domain.home.api.HomeController;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.user.api.UserController;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthController;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;
import com.simleetag.homework.api.domain.work.CategoryType;
import com.simleetag.homework.api.domain.work.api.*;
import com.simleetag.homework.api.domain.work.task.api.TaskCreateRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskMaintenanceController;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupMaintenanceController;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Profile("default")
@Configuration
@Transactional
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {
    // user
    private final OAuthController oauthController;
    private final UserController userController;

    // home
    private final HomeController homeController;

    // work
    private final CategoryMaintenanceController categoryMaintenanceController;
    private final TaskGroupMaintenanceController taskGroupMaintenanceController;
    private final TaskMaintenanceController taskMaintenanceController;
    private Long homeId;
    private String homeworkToken;
    private Long userId;

    @Override
    public void run(String... args) {
        // 유저 생성 및 정보 수정
        final TokenResponse user = oauthController.login(new TokenRequest("a.b.c", ProviderType.KAKAO)).getBody();
        homeworkToken = user.homeworkToken();
        userId = user.user().userId();
        userController.editProfile(userId, new UserProfileRequest("에버", "image.com"));

        // 집 생성 및 입장
        homeId = homeController.createHome(userId, new CreateHomeRequest("백엔드 집")).getBody().homeId();

        // 기본 카테고리 및 집안일 등록
        addAllDefaultCategoryWithTaskGroup();

        // 일회성 집안일 샘플 등록
        addTemporaryTaskSample();
    }

    private void addAllDefaultCategoryWithTaskGroup() {
        final DefaultCategoryWithTaskCreateRequest request = new DefaultCategoryWithTaskCreateRequest(
                Arrays.asList(
                        new CategoryWithTaskCreateRequest(
                                new CategoryCreateRequest(homeId, "🍚 요리", CategoryType.DEFAULT),
                                new TaskGroupCreateRequest("아침 식사 준비", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("점심 식사 준비", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("저녁 식사 준비", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("설거지", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("장보기", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("냉장고 정리", TaskGroupType.ROUTINE)
                        ),
                        new CategoryWithTaskCreateRequest(
                                new CategoryCreateRequest(homeId, "✨ 청소", CategoryType.DEFAULT),
                                new TaskGroupCreateRequest("분리수거", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("음식물 쓰레기 버리기", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("청소기 돌리기", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("물걸레질", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("화장실 청소", TaskGroupType.ROUTINE)
                        ),
                        new CategoryWithTaskCreateRequest(
                                new CategoryCreateRequest(homeId, "빨래", CategoryType.DEFAULT),
                                new TaskGroupCreateRequest("빨랫감 수거", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("빨래 돌리기", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("건조대 널기", TaskGroupType.ROUTINE),
                                new TaskGroupCreateRequest("빨래 개기", TaskGroupType.ROUTINE)
                        )
                )
        );
        categoryMaintenanceController.addAllDefaultCategoryWithTaskGroup(request);
    }

    private void addTemporaryTaskSample() {
        final Long temporaryCategoryId =
                categoryMaintenanceController.add(
                        new CategoryCreateRequest(homeId, "일회성 집안일", CategoryType.TEMPORARY)).getBody();

        final Long temporaryTaskGroupId = taskGroupMaintenanceController.add(
                new TaskGroupCreateRequest("일회성 집안일", TaskGroupType.TEMPORARY), temporaryCategoryId).getBody();

        taskMaintenanceController.add(new TaskCreateRequest(userId), temporaryCategoryId, temporaryTaskGroupId);
    }
}
