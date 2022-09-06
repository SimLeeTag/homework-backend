package com.simleetag.homework.api.domain.task.api;

public record CategoryWithTaskCreateRequest(
        CategoryCreateRequest categoryCreateRequest,
        TaskGroupCreateRequest... taskGroupCreateRequest
) {

}
