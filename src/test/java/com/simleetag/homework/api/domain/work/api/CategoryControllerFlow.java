package com.simleetag.homework.api.domain.work.api;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.common.FlowSupport;
import com.simleetag.homework.api.common.IdentifierHeader;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerFlow extends FlowSupport {

    public CategoryControllerFlow(MockMvc mockMvc) {
        super(mockMvc);
    }

    public List<CategoryResources.Reply.MeWithTaskGroup> findAllWithTaskGroup(String invitation) throws Exception {
        final String responseBody = mockMvc.perform(
                                                   get("/api/categories")
                                                           .header(IdentifierHeader.HOME.getKey(), invitation)
                                           ).andExpect(
                                                   status().isOk()
                                           )
                                           .andReturn()
                                           .getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        return Arrays.asList(objectMapper.readValue(responseBody, CategoryResources.Reply.MeWithTaskGroup[].class));
    }

    public void createNewCategory(String invitation, List<CategoryResources.Request.Create> request) throws Exception {
        mockMvc.perform(
                       post("/api/categories")
                               .contentType(MediaType.APPLICATION_JSON)
                               .header(IdentifierHeader.HOME.getKey(), invitation)
                               .content(objectMapper.writeValueAsString(request))
               ).andExpect(
                       status().isOk()
               )
               .andReturn()
               .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    public List<TaskResponse> findAllWithDate(String invitation, LocalDate date, Long memberId) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", date.toString());
        params.add("memberId", memberId.toString());
        final String responseBody = mockMvc.perform(
                       get("/api/categories/tasks")
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
