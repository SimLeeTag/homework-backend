package com.simleetag.homework.api.domain.task.api;

import com.simleetag.homework.api.domain.task.CategoryType;

public record CategoryCreateRequest(
        Long homeId,
        String name,
        CategoryType type
) {
}
