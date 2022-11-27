package com.simleetag.homework.api.domain.work.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    // 나의 집안일 하위 상태
    COMPLETED("완료"),
    UNFINISHED("미완료");

    private final String description;
}
