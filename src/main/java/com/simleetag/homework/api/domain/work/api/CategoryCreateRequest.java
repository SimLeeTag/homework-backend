package com.simleetag.homework.api.domain.work.api;

import com.simleetag.homework.api.domain.work.CategoryType;

public record CategoryCreateRequest(
        Long homeId,
        String name,
        CategoryType type
) {
}
