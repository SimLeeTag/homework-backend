package com.simleetag.homework.api.domain.work.taskGroup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cycle {
    private final List<DayOfWeek> dayOfWeeks = new ArrayList<>();

    private LocalDate startDate;

    private int term;
}
