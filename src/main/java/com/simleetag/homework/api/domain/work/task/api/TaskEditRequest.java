package com.simleetag.homework.api.domain.work.task.api;

import java.time.LocalDate;

import com.simleetag.homework.api.domain.work.task.TaskStatus;

public record TaskEditRequest(
        TaskStatus taskStatus,
        LocalDate dueDate
) {
}
