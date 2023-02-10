package com.simleetag.homework.api.domain.work.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import com.simleetag.homework.api.domain.work.task.api.TaskControllerFlow;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;
import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest extends TestSupport {

    private CategoryControllerFlow categoryController;

    private MemberControllerFlow memberController;

    private TaskControllerFlow taskController;

    private HomeControllerFlow homeController;

    private String everHomeworkToken;

    private Long everUserId;

    private Long everMemberId;

    private CreatedHomeResponse home;

    private OAuthControllerFlow oauthController;

    private UserControllerFlow userController;


    @BeforeAll
    public void init() {
        oauthController = new OAuthControllerFlow(mockMvc);
        userController = new UserControllerFlow(mockMvc);
        categoryController = new CategoryControllerFlow(mockMvc);
        homeController = new HomeControllerFlow(mockMvc);
        taskController = new TaskControllerFlow(mockMvc);
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
    class findCategoryTest {

        @Test
        @DisplayName("디폴트 카테고리 조회 성공 테스트")
        void getDefaultCategory() throws Exception {

            // given
            // 푸글 유저 생성
            var loginRequest = new TokenRequest("a.b.c", ProviderType.KAKAO);
            final TokenResponse poogle = oauthController.login(loginRequest);

            // 푸글 정보 수정
            final UserProfileRequest poogleProfile = new UserProfileRequest("푸글", "https://image.com");
            userController.editProfile(poogle.homeworkToken(), poogleProfile);

            // when
            // 푸글 집 들어가기
            String poogleHomeworkToken = poogle.homeworkToken();
            homeController.joinHome(home.homeId(), poogleHomeworkToken);

            // then
            final int defaultCategorySize = categoryController.findAllWithTaskGroup(home.invitation()).size();
            assertThat(defaultCategorySize).isEqualTo(3);
        }
    }

    @Nested
    class createCategoryTest {

        @Test
        @DisplayName("일회성 집안일 카테고리 생성 성공 테스트")
        void createCategory() throws Exception {

            // given
            // 일회성 집안일 카테고리
            final List<CategoryResources.Request.Create> request = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(new ArrayList<>(), null, null), Difficulty.LOW, 1L, everMemberId);
            request.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));

            // when
            // 일회성 카테고리, 집안일 추가
            // 변경 없는 디폴트 집안일은 추가 안해서 삭제
            categoryController.createNewCategory(home.invitation(), request);

            // then
            final int defaultCategorySize = categoryController.findAllWithTaskGroup(home.invitation()).size();
            assertThat(defaultCategorySize).isEqualTo(1);
        }
    }


    @Nested
    class deleteCategoryTest {

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategorySuccess() throws Exception {

            //given
            final List<CategoryResources.Request.Create> request = new ArrayList<>();
            CategoryResources.Request.Create.CategoryCreateRequest categoryCreateRequest = new CategoryResources.Request.Create.CategoryCreateRequest(null, "새로운 일회성 카테고리");
            CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupCreateRequest = new CategoryResources.Request.Create.TaskGroupCreateRequest(null, "일회성 집안일", TaskGroupType.TEMPORARY, new Cycle(Collections.singletonList(LocalDate.now().getDayOfWeek()), LocalDate.now(), 0), Difficulty.LOW, 1L, everMemberId);
            request.add(new CategoryResources.Request.Create(categoryCreateRequest, taskGroupCreateRequest));

            // when
            categoryController.createNewCategory(home.invitation(), request);
            CategoryResources.Reply.MeWithTaskGroup createdCategory = categoryController.findAllWithTaskGroup(home.invitation()).stream().filter(category -> category.categoryName().equals("새로운 일회성 카테고리")).findFirst().get();
            categoryController.deleteCategory(home.invitation(), createdCategory.categoryId());

            // then
            Optional<CategoryResources.Reply.MeWithTaskGroup> deletedCategory = categoryController.findAllWithTaskGroup(home.invitation()).stream().filter(category -> category.categoryId().equals(createdCategory.categoryId())).findAny();
            List<TaskResponse> tasks = memberController.findAllWithDueDate(home.invitation(), LocalDate.now(), everMemberId);

            assertAll(
                    () -> assertThat(deletedCategory).isEmpty(),
                    () -> assertThat(tasks.size()).isEqualTo(0)
            );
        }

        @Test
        @DisplayName("카테고리 삭제 실패 - 디폴트 카테고리 삭제 불가")
        void deleteCategoryFail() throws Exception {

            //given
            final String message = "해당 카테고리는 삭제가 불가능합니다.";

            // when
            Long defaultCategoryId = categoryController.findAllWithTaskGroup(home.invitation()).get(0).categoryId();

            // then
            final String response = categoryController.deleteCategoryFail(home.invitation(), defaultCategoryId, status().is5xxServerError());
            assertThat(response).isEqualTo(message);
        }
    }
}
