package com.simleetag.homework.api.domain.work.api;

import java.util.List;

public record DefaultTaskGroupCreateRequest(
        List<TaskGroupCreateRequest> taskGroups
) {

}
