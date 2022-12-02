package com.simleetag.homework.api.domain.work.taskGroup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record Cycle(
        List<DayOfWeek> dayOfWeeks,
        LocalDate startDate,
        Integer period
) {}
