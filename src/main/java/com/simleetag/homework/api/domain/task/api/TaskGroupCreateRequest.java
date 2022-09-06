package com.simleetag.homework.api.domain.task.api;

import com.simleetag.homework.api.domain.task.TaskGroupType;

public record TaskGroupCreateRequest(
        String name,
        TaskGroupType type
) {

}
