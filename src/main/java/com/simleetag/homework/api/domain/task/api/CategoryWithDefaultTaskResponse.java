package com.simleetag.homework.api.domain.task.api;

import java.util.List;

import com.simleetag.homework.api.domain.task.Category;
import com.simleetag.homework.api.domain.task.TaskResponse;

public record CategoryWithDefaultTaskResponse(
        Long categoryId,
        String categoryName,
        List<TaskResponse> tasks
) {

    public static CategoryWithDefaultTaskResponse from(Category category, List<TaskResponse> taskResponse) {
        return new CategoryWithDefaultTaskResponse(
                category.getId(),
                category.getName(),
                taskResponse
        );
    }
}
