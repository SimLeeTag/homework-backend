package com.simleetag.homework.api.domain.work.taskGroup.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.MemberControllerFlow;
import com.simleetag.homework.api.domain.home.member.api.MemberController;
import com.simleetag.homework.api.domain.user.api.UserControllerFlow;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;
import com.simleetag.homework.api.domain.work.api.CategoryControllerFlow;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;
import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskGroupControllerTest extends TestSupport {

    private TaskGroupControllerFlow taskGroupController;

    private CategoryControllerFlow categoryController;

    private HomeControllerFlow homeController;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;

    private MemberControllerFlow memberController;

    private Long everUserId;

    private Long everMemberId;

    private Long poogleMemberId;

    private CreatedHomeResponse home;

    private String everHomeworkToken;

    private String poogleHomeworkToken;


    @BeforeAll
    public void init() {
        taskGroupController = new TaskGroupControllerFlow(mockMvc);
        categoryController = new CategoryControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        memberController = new MemberControllerFlow(mockMvc);
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

        // 푸글 유저 생성
        final TokenResponse poogle = oauthController.login(loginRequest);
        poogleHomeworkToken = poogle.homeworkToken();

        // 푸글 정보 수정
        final UserProfileRequest poogleProfile = new UserProfileRequest("푸글", "https://image.com");
        userController.editProfile(poogleHomeworkToken, poogleProfile);

        // 푸글 집 들어가기
        poogleMemberId = homeController.joinHome(home.homeId(), poogleHomeworkToken).memberId();

    }

    @Nested
    class editTaskGroupTest {

        @Test
        @DisplayName("집안일 꾸러미 변경 성공 테스트")
        void changeTaskGroupSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskGroupEditRequest taskGroupEditRequest = new TaskGroupEditRequest("변경된 일회성 집안일", new Cycle(Collections.singletonList(LocalDate.now().plusDays(1).getDayOfWeek()), LocalDate.now().plusDays(1), 0), Difficulty.HIGH, everMemberId);
            Long taskGroupId = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().get(0).taskGroupId();
            TaskGroupResponse response = taskGroupController.editTaskGroup(home.invitation(), taskGroupId, taskGroupEditRequest);

            // then
            TaskGroupResponse changedTaskGroup = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().get(0);

            assertAll(
                    () -> assertThat(response.taskGroupName()).isEqualTo(changedTaskGroup.taskGroupName()),
                    () -> assertThat(response.cycle()).isEqualTo(changedTaskGroup.cycle()),
                    () -> assertThat(response.difficulty()).isEqualTo(changedTaskGroup.difficulty())
            );
        }

        @Test
        @DisplayName("집안일 꾸러미 변경 실패 테스트 - 일회성 집안일 담당자 변경")
        void changeTaskGroupFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "해당 집안일 꾸러미는 수정이 불가능합니다.";

            // when
            TaskGroupEditRequest taskGroupEditRequest = new TaskGroupEditRequest("변경된 일회성 집안일", new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, poogleMemberId);
            Long taskGroupId = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().get(0).taskGroupId();
            final String response = taskGroupController.editTaskGroupFail(home.invitation(), taskGroupId, taskGroupEditRequest, status().is5xxServerError());

            // then
            assertThat(response).isEqualTo(message);
        }
    }

    @Nested
    class deleteTaskGroupTest {

        @Test
        @DisplayName("집안일 꾸러미 삭제 성공 테스트")
        void deleteTaskGroupSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskGroupResponse taskGroupResponse = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().get(0);
            int taskGroupsSizeBeforeDeleted = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().size();

            // then
            TaskGroupResponse deletedTaskGroupResponse = taskGroupController.deleteTaskGroup(home.invitation(), taskGroupResponse.taskGroupId());
            int taskGroupsSizeAfterDeleted = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().size();
            List<TaskResponse> tasks = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId);

            assertAll(
                    () -> assertThat(taskGroupResponse.taskGroupId()).isEqualTo(deletedTaskGroupResponse.taskGroupId()),
                    () -> assertThat(taskGroupsSizeBeforeDeleted - 1).isEqualTo(taskGroupsSizeAfterDeleted),
                    () -> assertThat(tasks.size()).isEqualTo(0)
            );
        }

        @Test
        @DisplayName("집안일 꾸러미 삭제 실패 테스트 - 이미 삭제된 집안일")
        void deleteTaskFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "[%d] ID에 해당하는 집안일 꾸러미가 존재하지 않습니다.";

            // when
            TaskGroupResponse taskGroupResponse = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups().get(0);
            taskGroupController.deleteTaskGroup(home.invitation(), taskGroupResponse.taskGroupId());
            final String response = taskGroupController.deleteTaskGroupFail(home.invitation(), taskGroupResponse.taskGroupId(), status().is4xxClientError());
            List<TaskGroupResponse> taskGroups = categoryController.findAllWithTaskGroup(home.invitation()).get(0).taskGroups();
            List<TaskResponse> tasks = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId);

            // then
            assertAll(
                    () -> assertThat(response).isEqualTo(String.format(message, taskGroupResponse.taskGroupId())),
                    () -> assertThat(taskGroups.size()).isEqualTo(0),
                    () -> assertThat(tasks.size()).isEqualTo(0)
            );
        }
    }

}
