package com.simleetag.homework.api.domain.work.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    // 나의 집안일 하위 상태
    COMPLETED("완료"),
    UNFINISHED("미완료"),

    // 부탁한 집안일 및 부탁받은 집안일 공통 하위 상태
    ACCEPTED("수락"),
    DENIED("거절"),
    CANCELED("취소"),

    // 부탁한 집안일 하위 상태
    PENDING("부탁중"),

    // 부탁받은 집안일 하위 상태
    WAITING("미응답"),
    TIME_OUT("시간 초과");

    private final String description;
}
