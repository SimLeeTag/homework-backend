package com.simleetag.homework.api.domain.home.member;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.domain.home.member.dto.MemberResponse;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberMaintenanceControllerFlow extends FlowSupport {
    public MemberMaintenanceControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
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
}
