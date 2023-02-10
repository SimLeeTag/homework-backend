package com.simleetag.homework.api.domain.work.task.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.simleetag.homework.api.common.TestSupport;
import com.simleetag.homework.api.domain.home.api.HomeControllerFlow;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.member.MemberControllerFlow;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest extends TestSupport {

    private TaskControllerFlow taskController;

    private CategoryControllerFlow categoryController;

    private MemberControllerFlow memberController;

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
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskStatus(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            assertThat(taskResponse.taskStatus()).isEqualTo(changedTask.taskStatus());
        }
    }

    @Nested
    class changeTaskDueDateTest {

        @Test
        @DisplayName("집안일 마감일 변경 성공 테스트 - 미완료 오늘 집안일 마감일을 미래로 수정")
        void changeUnfinishedTaskDueDateNowToFutureSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now().plusDays(1));
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskDueDate(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(1), everMemberId).get(0);
            assertThat(taskResponse.dueDate()).isEqualTo(changedTask.dueDate());
        }

        @Test
        @DisplayName("집안일 마감일 변경 성공 테스트 - 미완료 미래 집안일 마감일을 오늘로 수정")
        void changeUnfinishedTaskDueDateFutureToNowSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().plusDays(1).getDayOfWeek()), LocalDate.now().plusDays(1), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now());
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(1), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskDueDate(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            assertThat(taskResponse.dueDate()).isEqualTo(changedTask.dueDate());
        }

        @Test
        @DisplayName("집안일 마감일 변경 성공 테스트 - 미완료 먼 미래 집안일 마감일을 가까운 미래로 수정")
        void changeUnfinishedTaskDueDateVeryFutureToFutureSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().plusDays(2).getDayOfWeek()), LocalDate.now().plusDays(2), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now().plusDays(1));
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(2), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskDueDate(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(1), everMemberId).get(0);
            assertThat(taskResponse.dueDate()).isEqualTo(changedTask.dueDate());
        }

        @Test
        @DisplayName("집안일 마감일 변경 성공 테스트 - 미완료 가까운 미래 집안일 마감일을 먼 미래로 수정")
        void changeUnfinishedTaskDueDateFutureToVeryFutureSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().plusDays(1).getDayOfWeek()), LocalDate.now().plusDays(1), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now().plusDays(2));
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(1), everMemberId).get(0).taskId();
            TaskResponse taskResponse = taskController.changeTaskDueDate(home.invitation(), taskId, taskEditRequest);

            // then
            TaskResponse changedTask = memberController.findAllWithDueDate(home.invitation(), LocalDate.now().plusDays(2), everMemberId).get(0);
            assertThat(taskResponse.dueDate()).isEqualTo(changedTask.dueDate());
        }

        @Test
        @DisplayName("집안일 마감일 변경 실패 테스트 - 완료한 집안일의 날짜 변경")
        void changeCompletedTaskDueDateFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "해당 집안일은 수정이 불가능합니다.";

            // when
            TaskStatusEditRequest taskStatusEditRequest = new TaskStatusEditRequest(TaskStatus.COMPLETED);
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            taskController.changeTaskStatus(home.invitation(), taskId, taskStatusEditRequest);
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now().plusDays(1));
            final String response = taskController.changeTaskDueDateFail(home.invitation(), taskId, taskEditRequest, status().is5xxServerError());

            // then
            assertThat(response).isEqualTo(message);
        }

        @Test
        @DisplayName("집안일 마감일 변경 실패 테스트 - 미완료 오늘 집안일 마감일을 과거로 수정")
        void changeTaskWrongDueDateFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "해당 집안일은 수정이 불가능합니다.";

            // when
            Long taskId = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0).taskId();
            TaskDueDateEditRequest taskEditRequest = new TaskDueDateEditRequest(LocalDate.now().minusDays(1));
            final String response = taskController.changeTaskDueDateFail(home.invitation(), taskId, taskEditRequest, status().is5xxServerError());

            // then
            assertThat(response).isEqualTo(message);
        }
    }

    @Nested
    class deleteTaskTest {

        @Test
        @DisplayName("집안일 삭제 성공 테스트")
        void deleteTaskSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskResponse task = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            TaskResponse taskResponse = taskController.deleteTask(home.invitation(), task.taskId());
            List<TaskResponse> tasks = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId);

            // then
            assertAll(
                    () -> assertThat(taskResponse.taskId()).isEqualTo(task.taskId()),
                    () -> assertThat(tasks.size()).isEqualTo(0)
            );
        }

        @Test
        @DisplayName("집안일 삭제 실패 테스트 - 이미 삭제된 집안일")
        void deleteTaskFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "[%d] ID에 해당하는 집안일이 존재하지 않습니다.";

            // when
            TaskResponse task = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            taskController.deleteTask(home.invitation(), task.taskId());
            final String response = taskController.deleteTaskFail(home.invitation(), task.taskId(), status().is4xxClientError());

            // then
            assertThat(response).isEqualTo(String.format(message, task.taskId()));
        }
    }

    @Nested
    class findOneTest {

        @Test
        @DisplayName("집안일 조회 성공 테스트")
        void findTaskSuccessTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);

            // when
            TaskResponse task = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            TaskResponse foundTask = taskController.findOne(home.invitation(), task.taskId());

            // then
            assertAll(
                    () -> assertThat(foundTask.taskName()).isEqualTo("일회성 집안일"),
                    () -> assertThat(foundTask.taskType()).isEqualTo(TaskGroupType.TEMPORARY),
                    () -> assertThat(foundTask.taskStatus()).isEqualTo(TaskStatus.UNFINISHED),
                    () -> assertThat(foundTask.dueDate()).isEqualTo(LocalDate.now()),
                    () -> assertThat(foundTask.difficulty()).isEqualTo(Difficulty.LOW),
                    () -> assertThat(foundTask.point()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("집안일 조회 실패 테스트")
        void findTaskFailTest() throws Exception {

            // given
            final List<CategoryResources.Request.Create> createRequest = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            createRequest.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));
            categoryController.createNewCategory(home.invitation(), createRequest);
            final String message = "[%d] ID에 해당하는 집안일이 존재하지 않습니다.";

            // when
            TaskResponse task = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId).get(0);
            taskController.deleteTask(home.invitation(), task.taskId());
            final String response = taskController.findOneFail(home.invitation(), task.taskId(), status().is4xxClientError());

            // then
            assertThat(response).isEqualTo(String.format(message, task.taskId()));
        }
    }

}
