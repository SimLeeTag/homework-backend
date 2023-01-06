package com.simleetag.homework.api.domain.home.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.domain.home.api.dto.EmptyHomeCreateRequest;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeMaintenanceControllerFlow extends FlowSupport {
    public HomeMaintenanceControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public HomeResponse create(EmptyHomeCreateRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/maintenance/homes")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, HomeResponse.class);
    }

    public MemberResponse join(Long homeId, Long userId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/maintenance/homes/{homeId}/members", homeId)
                                                           .param("userId", userId.toString())
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, MemberResponse.class);
    }

    public MemberResponse findOne(Long homeId, Long memberId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/maintenance/homes/{homeId}/members/{memberId}", homeId, memberId)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, MemberResponse.class);
    }

    public MemberResponse expire(Long homeId, Long memberId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   delete("/maintenance/homes/{homeId}/members/{memberId}", homeId, memberId)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, MemberResponse.class);
    }

}
