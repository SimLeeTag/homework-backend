package com.simleetag.homework.api.domain.work.task.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerFlow extends FlowSupport {

    public TaskControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public TaskResponse changeTaskStatus(String invitation, Long taskId, TaskStatusEditRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/api/tasks/{taskId}", taskId)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, TaskResponse.class);
    }
}
