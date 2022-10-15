package com.simleetag.homework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonMapper {
    private static final ObjectMapper MAPPER =
            new ObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                              .registerModule(new JavaTimeModule());

    private JsonMapper() {}

    public static ObjectMapper getJsonMapper() {
        return MAPPER;
    }

    public static <T> String writeValueAsString(T object) {
        String json;
        try {
            json = MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static <T> T readValue(String content, Class<T> clazz) {
        T value;
        try {
            value = MAPPER.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return value;
    }
}
