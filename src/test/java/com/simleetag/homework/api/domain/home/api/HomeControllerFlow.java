package com.simleetag.homework.api.domain.home.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.common.exception.Error;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerFlow extends FlowSupport {
    public HomeControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public CreatedHomeResponse createHome(String homeworkToken, HomeCreateRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/api/homes")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, CreatedHomeResponse.class);
    }

    public String createHomeFail(String homeworkToken, HomeCreateRequest request, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/api/homes")
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

    public HomeWithMembersResponse findMembersByToken(String homeworkToken, String invitation) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/api/homes")
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, HomeWithMembersResponse.class);
    }

    public String findMembersByTokenFail(String homeworkToken, String invitation, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/api/homes")
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                           ).andExpect(
                                                   matcher
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, Error.class).message();
    }

}
