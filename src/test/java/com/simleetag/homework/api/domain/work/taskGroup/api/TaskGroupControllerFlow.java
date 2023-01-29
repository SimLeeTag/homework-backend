package com.simleetag.homework.api.domain.work.taskGroup.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.common.exception.Error;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskGroupControllerFlow extends FlowSupport {

    public TaskGroupControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public TaskGroupResponse editTaskGroup(String invitation, Long taskGroupId, TaskGroupEditRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   put("/api/task-groups/{taskGroupId}", taskGroupId)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                                           .content(objectMapper.writeValueAsString(request))
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, TaskGroupResponse.class);
    }

    public String editTaskGroupFail(String invitation, Long taskGroupId, TaskGroupEditRequest request, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   put("/api/task-groups/{taskGroupId}", taskGroupId)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
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
