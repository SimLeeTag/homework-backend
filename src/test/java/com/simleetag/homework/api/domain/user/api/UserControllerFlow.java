package com.simleetag.homework.api.domain.user.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.exception.Error;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerFlow extends FlowSupport {
    public UserControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public UserControllerFlow(MockMvc mockMvc, boolean document) {
        super(mockMvc, document);
    }

    public UserWithHomesResponse findUserByAccessToken(String homeworkToken) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/users/me")
                                                           .with(mockMvc.userToken(homeworkToken))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, UserWithHomesResponse.class);
    }

    public String findUserByAccessTokenFail(String homeworkToken, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/users/me")
                                                           .with(mockMvc.userToken(homeworkToken))
                                           ).andExpect(
                                                   matcher
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, Error.class).message();
    }

    public UserProfileResponse editProfile(String homeworkToken, UserProfileRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/users/me")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .with(mockMvc.userToken(homeworkToken))
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, UserProfileResponse.class);
    }

    public String editProfileFail(String homeworkToken, UserProfileRequest request, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/users/me")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .with(mockMvc.userToken(homeworkToken))
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   matcher
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, Error.class).message();
    }
}
