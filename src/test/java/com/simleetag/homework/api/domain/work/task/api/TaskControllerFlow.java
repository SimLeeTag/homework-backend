package com.simleetag.homework.api.domain.work.task.api;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.common.exception.Error;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerFlow extends FlowSupport {

    public TaskControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public TaskResponse changeTaskStatus(String invitation, Long taskId, TaskStatusEditRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/api/tasks/{taskId}/change-status", taskId)
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

    public TaskResponse changeTaskDueDate(String invitation, Long taskId, TaskDueDateEditRequest request) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/api/tasks/{taskId}/change-duedate", taskId)
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

    public String changeTaskDueDateFail(String invitation, Long taskId, TaskDueDateEditRequest request, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   patch("/api/tasks/{taskId}/change-duedate", taskId)
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

    public TaskResponse deleteTask(String invitation, Long taskId) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   delete("/api/tasks/{taskId}", taskId)
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readValue(responseBody, TaskResponse.class);
    }

    public String deleteTaskFail(String invitation, Long taskId, ResultMatcher matcher) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   delete("/api/tasks/{taskId}", taskId)
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
