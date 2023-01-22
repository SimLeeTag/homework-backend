package com.simleetag.homework.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.simleetag.homework.api.domain.home.api.HomeController;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.api.MemberMaintenanceController;
import com.simleetag.homework.api.domain.user.api.UserMaintenanceController;
import com.simleetag.homework.api.domain.user.api.UserSignUpRequest;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceController;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceResources;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.task.api.TaskCreateRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskEditRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskMaintenanceController;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupMaintenanceController;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupMaintenanceResources;

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
    private final UserMaintenanceController userMaintenanceController;

    private final MemberMaintenanceController memberMaintenanceController;
    // home
    private final HomeController homeController;

    // work
    private final CategoryMaintenanceController categoryMaintenanceController;

    private final TaskGroupMaintenanceController taskGroupMaintenanceController;

    private final TaskMaintenanceController taskMaintenanceController;

    private Long homeId;

    private Long userId;

    private Long memberId;

    @Override
    public void run(String... args) {
        // 유저 생성 및 정보 수정
        userId = userMaintenanceController.signUp(
                new UserSignUpRequest("dummy-oauth-id", "image.com", "ever")).getBody();

        // 집 생성 및 입장
        homeId = homeController.createHome(userId, new HomeCreateRequest("백엔드 집")).getBody().homeId();

        // 일회성 집안일 샘플 등록
        addTemporaryTaskSample();
    }

    private void addTemporaryTaskSample() {
        memberId = memberMaintenanceController.findMemberId(userId, homeId).getBody().memberId();
        final Long temporaryCategoryId =
                categoryMaintenanceController.add(
                        new CategoryMaintenanceResources.Request.Category(homeId, "일회성 집안일", Category.CategoryType.TEMPORARY)).getBody();

        final Long temporaryTaskGroupId = taskGroupMaintenanceController.add(temporaryCategoryId,
                new TaskGroupMaintenanceResources.Request.TaskGroup(
                        "일회성 집안일",
                        TaskGroupType.TEMPORARY,
                        null,
                        null,
                        null,
                        memberId
                )).getBody();
        final Long temporaryTaskId = taskMaintenanceController.add(new TaskCreateRequest(DayOfWeek.MONDAY), temporaryCategoryId, temporaryTaskGroupId).getBody();
        taskMaintenanceController.edit(new TaskEditRequest(TaskStatus.UNFINISHED, LocalDate.now()), temporaryCategoryId, temporaryTaskGroupId, temporaryTaskId);
    }
}
