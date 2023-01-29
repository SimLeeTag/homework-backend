package com.simleetag.homework.api.domain.work.task.api;

import java.time.LocalDate;

public record TaskDueDateEditRequest(
        LocalDate dueDate
) {
}
