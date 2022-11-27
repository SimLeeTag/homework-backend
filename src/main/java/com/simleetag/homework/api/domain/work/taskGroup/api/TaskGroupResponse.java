package com.simleetag.homework.api.domain.work.taskGroup.api;

import java.util.List;

import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

public record TaskGroupResponse(
        Long taskGroupId,
        String taskGroupName,
        TaskGroupType taskGroupType,
        Cycle cycle,
        Difficulty difficulty,
        Long point,
        Long ownerId
) {

    public static List<TaskGroupResponse> from(List<TaskGroup> taskGroups) {
        return taskGroups.stream()
                         .map(taskGroup -> new TaskGroupResponse(
                                 taskGroup.getId(),
                                 taskGroup.getName(),
                                 taskGroup.getType(),
                                 taskGroup.getCycle(),
                                 taskGroup.getDifficulty(),
                                 taskGroup.getPoint(),
                                 taskGroup.getOwner() == null ? null : taskGroup.getOwner().getId()))
                         .toList();
    }
}
