package com.simleetag.homework.api.domain.home.member;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskRateResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerFlow extends FlowSupport {
    public MemberControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public MemberIdResponse findMemberId(String homeworkToken, Long homeId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/api/members")
                                                           .param("homeId", String.valueOf(homeId))
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
                                           ).andExpect(
                                                   status().isOk()
                                           ).andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(responseBody, MemberIdResponse.class);
    }

    public List<TaskRateResponse> checkRatesWithDueDate(String invitation, LocalDate startDate, LocalDate endDate, Long memberId) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", startDate.toString());
        params.add("endDate", endDate.toString());
        final String responseBody = mockMvc.perform(
                                                   get("/api/members/{memberId}/tasks/rate", memberId)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                                           .params(params)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        return Arrays.asList(objectMapper.readValue(responseBody, TaskRateResponse[].class));
    }

    public List<TaskResponse> findAllWithDueDate(String invitation, LocalDate date, Long memberId) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", date.toString());
        params.add("memberId", memberId.toString());
        final String responseBody = mockMvc.perform(
                                                   get("/api/members/{memberId}/tasks", memberId)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                                           .params(params)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        return Arrays.asList(objectMapper.readValue(responseBody, TaskResponse[].class));
    }


}
