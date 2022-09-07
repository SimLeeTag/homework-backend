package com.simleetag.homework.api.domain.work.api;

import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

public record TaskGroupCreateRequest(
        String name,
        TaskGroupType type
) {

}
