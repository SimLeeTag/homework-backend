package com.simleetag.homework.api.domain.work.taskGroup.api;

import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;

public record TaskGroupEditRequest(
        String taskGroupName,
        Cycle cycle,
        Difficulty difficulty,
        Long ownerId
) {
}
