package com.simleetag.homework.api.domain.task;

import java.util.List;

public record TaskResponse(
        Long taskId,
        String taskName
) {

    public static List<TaskResponse> from(List<TaskGroup> taskGroups) {
        return taskGroups.stream()
                         .map(taskGroup -> new TaskResponse(taskGroup.getId(), taskGroup.getName()))
                         .toList();
    }
}
