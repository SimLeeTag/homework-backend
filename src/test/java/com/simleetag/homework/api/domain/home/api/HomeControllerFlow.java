package com.simleetag.homework.api.domain.home.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.exception.Error;
import com.simleetag.homework.api.domain.home.api.dto.CreateHomeRequest;
import com.simleetag.homework.api.domain.home.api.dto.CreatedHomeResponse;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerFlow extends FlowSupport {
    public HomeControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public HomeControllerFlow(MockMvc mockMvc, boolean document) {
        super(mockMvc, document);
    }

    public CreatedHomeResponse createHome(String homeworkToken, CreateHomeRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/homes")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .with(mockMvc.userToken(homeworkToken))
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, CreatedHomeResponse.class);
    }

    public String createHomeFail(String homeworkToken, CreateHomeRequest request, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/homes")
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

    public HomeResponse findMembersByToken(String homeworkToken, String invitation) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/homes")
                                                           .with(mockMvc.userToken(homeworkToken))
                                                           .with(mockMvc.invitation(invitation))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, HomeResponse.class);
    }

    public String findMembersByTokenFail(String homeworkToken, String invitation, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/homes")
                                                           .with(mockMvc.userToken(homeworkToken))
                                                           .with(mockMvc.invitation(invitation))
                                           ).andExpect(
                                                   matcher
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, Error.class).message();
    }

    public MemberIdResponse joinHome(Long homeId, String homeworkToken) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/homes/" + homeId)
                                                           .with(mockMvc.userToken(homeworkToken))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, MemberIdResponse.class);
    }
}
