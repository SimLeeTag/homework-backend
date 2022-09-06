package com.simleetag.homework.api.domain.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.domain.task.api.CategoryCreateRequest;
import com.simleetag.homework.api.domain.task.api.CategoryWithTaskCreateRequest;
import com.simleetag.homework.api.domain.task.api.CategoryWithDefaultTaskResponse;
import com.simleetag.homework.api.domain.task.api.DefaultCategoryWithTaskCreateRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {
    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID 에 해당하는 카테고리가 존재하지 않습니다.";
    private final CategoryRepository categoryRepository;
    private final TaskGroupService taskGroupService;

    public List<CategoryWithDefaultTaskResponse> findAllDefaultCategoryWithTask() {
        final List<Category> categories = categoryRepository.findAllByType(CategoryType.DEFAULT);

        final List<CategoryWithDefaultTaskResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            final List<TaskResponse> tasks = TaskResponse.from(category.getTaskGroups());
            final CategoryWithDefaultTaskResponse categoryResponse = CategoryWithDefaultTaskResponse.from(category, tasks);
            responses.add(categoryResponse);
        }

        return responses;
    }

    public Category add(CategoryCreateRequest request) {
        return categoryRepository.save(new Category(request.homeId(), request.name(), request.type()));
    }

    public void addAllDefaultCategoryWithTaskGroup(DefaultCategoryWithTaskCreateRequest request) {
        for (CategoryWithTaskCreateRequest createRequest : request.categoryWithTaskCreateRequests()) {
            final Category category = add(createRequest.categoryCreateRequest());
            taskGroupService.addAllDefaultTaskGroup(category, createRequest.taskGroupCreateRequest());
        }
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                                 .orElseThrow(() -> new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, id)));
    }
}
