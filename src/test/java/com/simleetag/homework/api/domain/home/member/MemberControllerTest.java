package com.simleetag.homework.api.domain.home.member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.user.api.UserControllerFlow;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.oauth.ProviderType;
import com.simleetag.homework.api.domain.user.oauth.api.OAuthControllerFlow;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;
import com.simleetag.homework.api.domain.work.api.CategoryControllerFlow;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.task.api.TaskControllerFlow;
import com.simleetag.homework.api.domain.work.task.api.TaskRateResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskStatusEditRequest;
import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

import org.junit.jupiter.api.*;

import org.assertj.core.api.Assertions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberControllerTest extends TestSupport {

    private MemberControllerFlow memberController;

    private HomeControllerFlow homeController;

    private CategoryControllerFlow categoryController;

    private TaskControllerFlow taskController;

    private String everHomeworkToken;

    private Long everUserId;

    private Long homeId;

    private Long everMemberId;

    private CreatedHomeResponse home;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;


    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        memberController = new MemberControllerFlow(mockMvc);
        categoryController = new CategoryControllerFlow(mockMvc);
        taskController = new TaskControllerFlow(mockMvc);
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
        userController.editProfile(ever.homeworkToken(), everProfile);

        final String homeName = "백엔드집";
        final HomeCreateRequest request = new HomeCreateRequest(homeName);
        home = homeController.createHome(everHomeworkToken, request);
        homeId = home.homeId();

        // 에버 집 들어가기
        everMemberId = homeController.joinHome(home.homeId(), everHomeworkToken).memberId();
    }

    @Test
    @DisplayName("집 아이디와 유저 아이디로 멤버 아이디 조회 성공 테스트")
    void findMemberByHomeIdAndUserId() throws Exception {

        // given
        // 집 들어가기(멤버 추가)
        MemberIdResponse memberIdResponse = homeController.joinHome(homeId, everHomeworkToken);

        // when
        MemberIdResponse findMemberId = memberController.findMemberId(everHomeworkToken, homeId);

        // then
        assertThat(memberIdResponse.memberId()).isEqualTo(findMemberId.memberId());
    }

    @Nested
    class checkTaskRateTest {

        @Test
        @DisplayName("TaskStatus - 전체 UNFINISHED일 때 TaskRate 0 확인")
        void checkTaskRateAllUnfinished() throws Exception {

            //given
            final List<CategoryResources.Request.Create> request = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            request.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));

            // when
            categoryController.createNewCategory(home.invitation(), request);

            // then
            List<TaskRateResponse> taskRateResponse = memberController.checkRatesWithDueDate(home.invitation(), LocalDate.now(), LocalDate.now(), everMemberId);
            Assertions.assertThat(taskRateResponse.get(0).rate()).isEqualTo(0);
        }

        @Test
        @DisplayName("TaskStatus - 전체 COMPLETED일 때 TaskRate 100 확인")
        void checkTaskRateAllCompleted() throws Exception {

            //given
            final List<CategoryResources.Request.Create> request = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            request.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));

            // when
            categoryController.createNewCategory(home.invitation(), request);
            TaskStatusEditRequest taskEditRequest = new TaskStatusEditRequest(TaskStatus.COMPLETED);
            Long taskId = categoryController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            taskController.changeTaskStatus(home.invitation(), taskId, taskEditRequest);

            // then
            List<TaskRateResponse> taskRateResponse = memberController.checkRatesWithDueDate(home.invitation(), LocalDate.now(), LocalDate.now(), everMemberId);
            Assertions.assertThat(taskRateResponse.get(0).rate()).isEqualTo(100);
        }

        @Test
        @DisplayName("TaskStatus - 전체 중 1/3 COMPLETED일 때 TaskRate 33 확인")
        void checkTaskRatePartiallyCompleted() throws Exception {

            //given
            final List<CategoryResources.Request.Create> request = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            request.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest, taskGroupCreateRequest, taskGroupCreateRequest));

            // when
            categoryController.createNewCategory(home.invitation(), request);
            TaskStatusEditRequest taskStatusEditRequest = new TaskStatusEditRequest(TaskStatus.COMPLETED);
            Long taskId = categoryController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            taskController.changeTaskStatus(home.invitation(), taskId, taskStatusEditRequest);

            // then
            List<TaskRateResponse> taskRateResponse = memberController.checkRatesWithDueDate(home.invitation(), LocalDate.now(), LocalDate.now(), everMemberId);
            Assertions.assertThat(taskRateResponse.get(0).rate()).isEqualTo(33);
        }

    }


}
