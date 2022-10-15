package com.simleetag.homework.api.domain.home.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.domain.home.api.dto.HomeResponse;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeMaintenanceControllerFlow extends FlowSupport {
    public HomeMaintenanceControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public HomeResponse kickOutAll(Long homeId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/maintenance/homes/{homeId}/kick-out/all", homeId)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, HomeResponse.class);
    }
}
