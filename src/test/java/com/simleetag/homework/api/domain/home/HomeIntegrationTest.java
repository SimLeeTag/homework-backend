package com.simleetag.homework.api.domain.home;

import com.simleetag.homework.api.common.IntegrationTest;
import com.simleetag.homework.api.domain.home.dto.CreateHomeRequest;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeIntegrationTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuthJwt oauthJwt;

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
}
