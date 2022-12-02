package com.simleetag.homework.api.domain.work.task.api;

import java.time.DayOfWeek;

public record TaskCreateRequest(
        DayOfWeek dayOfWeek
) {
}
