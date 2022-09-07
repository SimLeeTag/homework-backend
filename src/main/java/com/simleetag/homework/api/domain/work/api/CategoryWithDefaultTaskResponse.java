package com.simleetag.homework.api.domain.work.api;

import java.util.List;

import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupResponse;

public record CategoryWithDefaultTaskResponse(
        Long categoryId,
        String categoryName,
        List<TaskGroupResponse> tasks
) {

    public static CategoryWithDefaultTaskResponse from(Category category, List<TaskGroupResponse> taskGroupResponse) {
        return new CategoryWithDefaultTaskResponse(
                category.getId(),
                category.getName(),
                taskGroupResponse
        );
    }
}
