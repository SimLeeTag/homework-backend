package com.simleetag.homework.api.domain.work.task.api;

import com.simleetag.homework.api.domain.work.task.TaskStatus;

public record TaskStatusEditRequest(
        TaskStatus taskStatus
) {
}
