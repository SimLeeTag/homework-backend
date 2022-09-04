package com.simleetag.homework.api.domain.user.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.common.exception.Error;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileResponse;
import com.simleetag.homework.api.domain.user.api.dto.UserWithHomesResponse;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerFlow extends FlowSupport {
    public UserControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public UserWithHomesResponse findUserByAccessToken(String homeworkToken) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/api/users/me")
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
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
                                                   get("/api/users/me")
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
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
                                                   patch("/api/users/me")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
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
                                                   patch("/api/users/me")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
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
