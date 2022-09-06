package com.simleetag.homework.api.domain.task.api;

import java.util.List;

public record DefaultCategoryWithTaskCreateRequest(
        List<CategoryWithTaskCreateRequest> categoryWithTaskCreateRequests
) {

}
