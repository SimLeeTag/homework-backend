package com.simleetag.homework.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simleetag.homework.api.common.mockmvc.RestDocsMockMvc;

import org.springframework.test.web.servlet.MockMvc;

public abstract class FlowSupport {
    protected final RestDocsMockMvc mockMvc;
    protected final ObjectMapper objectMapper;

    public FlowSupport(MockMvc mockMvc) {
        this.mockMvc = new RestDocsMockMvc(mockMvc, true);
        this.objectMapper = new ObjectMapper();
    }

    public FlowSupport(MockMvc mockMvc, boolean document) {
        this.mockMvc = new RestDocsMockMvc(mockMvc, document);
        this.objectMapper = new ObjectMapper();
    }
}
