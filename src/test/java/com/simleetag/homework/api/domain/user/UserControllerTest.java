package com.simleetag.homework.api.domain.user;

import java.time.LocalDateTime;

import com.simleetag.homework.api.domain.user.dto.UserRequest;
import com.simleetag.homework.api.utils.ControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @Autowired
    private UserController userController;

    @Test
    @DisplayName("AccessToken을 가진 유저 조회 테스트")
    void findUserByAccessToken() throws Exception {

        // given
        final long id = 1L;
        final String userName = "Ever";
        final String profileImage = "https://image.com/image.jpg";
        final User user = new User(id, LocalDateTime.now(), LocalDateTime.now(), "12345", "aaa.bbb.ccc", userName, profileImage);
        given(userService.findByAccessToken(any(String.class))).willReturn(user);

        String requestBody = objectMapper.writeValueAsString(new UserRequest("access.token.sample"));

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                post("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isOk())
                     .andExpect(jsonPath("$.id").value(id))
                     .andExpect(jsonPath("$.userName").value(userName))
                     .andExpect(jsonPath("$.profileImage").value(profileImage));
    }

    @Test
    @DisplayName("AccessToken을 가진 유저가 존재하지 않을 경우 테스트")
    void findUserByAccessNotExistToken() throws Exception {

        // given
        final String message = "액세스 토큰을 가진 유저가 존재하지 않습니다.";
        given(userService.findByAccessToken(any(String.class))).willThrow(new IllegalArgumentException(message));
        String requestBody = objectMapper.writeValueAsString(new UserRequest("access.token.sample"));

        // when
        ResultActions resultActions = this.successMockMvc.perform(
                post("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                     .andExpect(jsonPath("$.message").value(message));
    }
}
