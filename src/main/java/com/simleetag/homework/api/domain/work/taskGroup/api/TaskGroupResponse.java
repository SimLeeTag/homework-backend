package com.simleetag.homework.api.domain.work.taskGroup.api;

import java.util.List;

import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

public record TaskGroupResponse(
        Long taskGroupId,
        String taskGroupName
) {

    public static List<TaskGroupResponse> from(List<TaskGroup> taskGroups) {
        return taskGroups.stream()
                         .map(taskGroup -> new TaskGroupResponse(taskGroup.getId(), taskGroup.getName()))
                         .toList();
    }
}
