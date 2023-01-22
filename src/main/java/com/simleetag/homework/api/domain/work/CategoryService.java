package com.simleetag.homework.api.domain.work;

import java.time.LocalDate;
import java.util.List;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceController;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceResources;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.api.TaskResponse;
import com.simleetag.homework.api.domain.work.repository.CategoryDslRepository;
import com.simleetag.homework.api.domain.work.repository.CategoryRepository;
import com.simleetag.homework.api.domain.work.task.TaskDslRepository;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupRepository;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {
    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID 에 해당하는 카테고리가 존재하지 않습니다.";

    private final CategoryRepository categoryRepository;

    private final CategoryDslRepository categoryDslRepository;

    private final TaskDslRepository taskDslRepository;

    private final TaskGroupService taskGroupService;

    private final TaskGroupRepository taskGroupRepository;

    private final HomeFinder homeFinder;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                                 .orElseThrow(() -> new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, id)));
    }

    public List<Category> findAllWithTaskGroupByHomeId(Long homeId) {
        return categoryDslRepository.findAllWithTaskGroupByHomeId(homeId);
    }

    public List<Category> search(CategoryMaintenanceController.CategorySearchCondition condition) {
        return categoryRepository.findAll(condition.toSpecs());
    }

    public Category add(CategoryMaintenanceResources.Request.Category request) {
        final Home home = homeFinder.findHomeById(request.homeId());
        return categoryRepository.save(
                new Category(
                        request.name(),
                        request.type(),
                        home
                )
        );
    }

    public void setDefaultCategoriesAtHome(Home home) {
        new DefaultCategory(categoryRepository, taskGroupRepository).setDefaultCategoriesAtHome(home);
    }

    public void sync(Long homeId, List<CategoryResources.Request.Create> requests) {
        final List<Category> categories = categoryDslRepository.findAllWithTaskGroupByHomeId(homeId);
        final Home home = homeFinder.findHomeById(homeId);
        new CategorySync(categoryRepository, requests, categories).sync(home);
        taskGroupService.sync(requests, categories);
        home.initialize();
    }

    public List<TaskResponse> findAllTasksByDueDate(Long memberId, LocalDate date) {
        return TaskResponse.from(taskDslRepository.findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(memberId, date));
    }
}
