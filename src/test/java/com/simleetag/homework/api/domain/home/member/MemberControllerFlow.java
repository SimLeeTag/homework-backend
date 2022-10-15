package com.simleetag.homework.api.domain.home.member;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.domain.home.member.dto.MemberIdResponse;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerFlow extends FlowSupport {
    public MemberControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public MemberIdResponse joinHome(Long homeId, String homeworkToken) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   post("/api/homes/" + homeId)
                                                           .header(IdentifierHeader.USER.getKey(), homeworkToken)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, MemberIdResponse.class);
    }
}
