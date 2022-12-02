package com.simleetag.homework.api.domain.work.taskGroup.api;

import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

public record TaskGroupMaintenanceResources(
        Request request,
        Reply reply
) {
    public static class Request {
        public record TaskGroup(
                String name,
                TaskGroupType type,
                Cycle cycle,
                Difficulty difficulty,
                Long point,
                Long ownerId
        ) {}
    }

    public static class Reply {

    }
}
