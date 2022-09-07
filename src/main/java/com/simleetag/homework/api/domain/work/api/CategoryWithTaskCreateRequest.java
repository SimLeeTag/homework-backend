package com.simleetag.homework.api.domain.work.api;

public record CategoryWithTaskCreateRequest(
        CategoryCreateRequest categoryCreateRequest,
        TaskGroupCreateRequest... taskGroupCreateRequest
) {

}
