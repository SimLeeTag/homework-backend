package com.simleetag.homework.api.domain.home;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.infra.HomeJwt;
import com.simleetag.homework.api.domain.member.MemberResources;
import com.simleetag.homework.api.domain.member.MemberService;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserRepository;
import com.simleetag.homework.api.domain.user.UserResources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuthJwt oauthJwt;

    @Autowired
    private HomeJwt homeJwt;

    @Autowired
    private HomeRepository homeRepository;

    @Nested
    class createHomeTest {

        @Test
        @DisplayName("집 생성 성공 테스트")
        void createHome() throws Exception {

            // given
            final User user = userRepository.save(UserResources.aFixtureWithNoMembers(null));
            final String homeworkToken = oauthJwt.createHomeworkToken(user.getId());
            final CreateHomeRequest request = new CreateHomeRequest("백엔드집");

            // when
            ResultActions resultActions = successMockMvc.perform(
                    post("/homes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(successMockMvc.userToken(homeworkToken))
                            .content(objectMapper.writeValueAsBytes(request))
            );

            // then
            resultActions.andExpect(status().is2xxSuccessful());
        }

        @Test
        @DisplayName("최대 소속 가능 집 개수를 초과할 때 집 생성 테스트")
        void createHomeMoreThanThree() throws Exception {

            // given
            final User user = userRepository.save(UserResources.aFixtureWithMaximumMembers(null));
            final String homeworkToken = oauthJwt.createHomeworkToken(user.getId());
            final CreateHomeRequest request = new CreateHomeRequest("백엔드집");
            final String message = "최대 3개의 집에 소속될 수 있습니다.";

            // when
            ResultActions resultActions = failMockMvc.perform(
                    post("/homes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(successMockMvc.userToken(homeworkToken))
                            .content(objectMapper.writeValueAsBytes(request))
            );

            // then
            resultActions.andExpect(status().isBadRequest())
                         .andExpect(jsonPath("$.message").value(message));
        }

    }

    @Nested
    class getHomeInformationTest {

        @Test
        @DisplayName("집 조회 성공 테스트")
        void getHome() throws Exception {

            //given
            final User poogle = userRepository.save(UserResources.aFixtureWithNoMembers(null, "푸글"));
            final User ever = userRepository.save(UserResources.aFixtureWithNoMembers(null, "에버"));

            final Home backend = homeRepository.save(HomeResources.aFixtureWithNoMembers(null, "백엔드"));
            memberService.save(MemberResources.aFixture(null, ever, backend));
            memberService.save(MemberResources.aFixture(null, poogle, backend));

            final String invitation = homeJwt.createHomeworkToken(backend.getId());
            final String homeworkToken = oauthJwt.createHomeworkToken(poogle.getId());

            //when
            ResultActions resultActions = successMockMvc.perform(
                    get("/homes")
                            .with(successMockMvc.userToken(homeworkToken))
                            .with(successMockMvc.invitation(invitation))
            );

            //then
            resultActions.andExpect(status().isOk());
        }

        @Test
        @DisplayName("invitation 정보에 들어있는 home id가 존재하지 않을 경우 테스트")
        void findHomeByHomeIdNotExist() throws Exception {

            // given
            final User poogle = userRepository.save(UserResources.aFixtureWithNoMembers(null, "푸글"));
            final Long homeId = 10L;
            final String message = String.format("HomeID[%d]에 해당하는 집이 존재하지 않습니다.", homeId);
            final String invitation = homeJwt.createHomeworkToken(homeId);
            final String homeworkToken = oauthJwt.createHomeworkToken(poogle.getId());

            // when
            ResultActions resultActions = failMockMvc.perform(
                    get("/homes")
                            .with(failMockMvc.userToken(homeworkToken))
                            .with(failMockMvc.invitation(invitation))
            );

            //then
            resultActions.andExpect(status().isBadRequest())
                         .andExpect(jsonPath("$.message").value(message));
        }

    }

    @Nested
    class joinHomeTest {

        @Test
        @DisplayName("집 들어가기 성공 테스트")
        void joinHome() throws Exception {

            // given
            final User poogle = userRepository.save(UserResources.aFixtureWithNoMembers(null, "푸글"));
            final Home backend = homeRepository.save(HomeResources.aFixtureWithNoMembers(null, "백엔드"));

            final String homeworkToken = oauthJwt.createHomeworkToken(poogle.getId());

            // when
            ResultActions resultActions = successMockMvc.perform(
                    post("/homes/" + backend.getId())
                            .with(successMockMvc.userToken(homeworkToken))
            );

            // then
            resultActions.andExpect(status().is2xxSuccessful());
        }
    }
}
