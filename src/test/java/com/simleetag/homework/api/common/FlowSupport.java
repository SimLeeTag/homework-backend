package com.simleetag.homework.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.web.servlet.MockMvc;

public abstract class FlowSupport {
    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    public FlowSupport(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }
}
