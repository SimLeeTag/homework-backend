package com.simleetag.homework.api.domain.work.task.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.user.api.UserControllerFlow;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;
import com.simleetag.homework.api.domain.work.api.CategoryControllerFlow;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest extends TestSupport {

    private TaskControllerFlow taskController;

    private CategoryControllerFlow categoryController;

    private HomeControllerFlow homeController;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;

    private Long everUserId;

    private Long everMemberId;

    private CreatedHomeResponse home;

    private String everHomeworkToken;

    @BeforeAll
    public void init() {
        taskController = new TaskControllerFlow(mockMvc);
        categoryController = new CategoryControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 에버 유저 생성
        var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
        final TokenResponse ever = oauthController.login(loginRequest);

        everHomeworkToken = ever.homeworkToken();
        everUserId = ever.user().userId();

        // 에버 정보 수정
        final UserProfileRequest everProfile = new UserProfileRequest("에버", "https://image.com");
        userController.editProfile(everHomeworkToken, everProfile);

        // 집 생성
        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        home = homeController.createHome(everHomeworkToken, request);

        // 에버 집 들어가기
        everMemberId = homeController.joinHome(home.homeId(), everHomeworkToken).memberId();
    }

    @Nested
    class changeTaskStatusTest {

        @Test
        @DisplayName("집안일 수행 완료 확인 성공 테스트")
        void checkCompletedTask() throws Exception {

            //given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskStatusEditRequest taskEditRequest = new TaskStatusEditRequest(TaskStatus.COMPLETED);
            Long taskId = categoryController.findAllWithDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskStatus(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = categoryController.findAllWithDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            assertThat(taskResponse.taskStatus()).isEqualTo(changedTask.taskStatus());
        }
    }

}
