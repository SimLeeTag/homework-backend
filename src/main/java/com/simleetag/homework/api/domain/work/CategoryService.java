package com.simleetag.homework.api.domain.work;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.simleetag.homework.api.common.exception.HomeControlException;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeFinder;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceController;
import com.simleetag.homework.api.domain.work.api.CategoryMaintenanceResources;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.repository.CategoryDslRepository;
import com.simleetag.homework.api.domain.work.repository.CategoryRepository;
import com.simleetag.homework.api.domain.work.task.TaskDslRepository;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.task.api.TaskRateResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
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

    private static final String CANNOT_DELETE_EXCEPTION = "해당 카테고리는 삭제가 불가능합니다.";

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

    public List<CategoryResources.Reply.MeWithTaskGroup> findAllWithTaskGroupByHomeId(Long homeId) {
        List<Category> categories = categoryDslRepository.findAllWithTaskGroupByHomeId(homeId);
        List<CategoryResources.Reply.MeWithTaskGroup> filteredAllWithTaskGroup = new ArrayList<>();
        for (Category category : categories) {
            List<TaskGroup> filteredTaskGroups = category.getTaskGroups().stream().filter(taskGroup -> taskGroup.getDeletedAt() == null).collect(Collectors.toList());
            filteredAllWithTaskGroup.add(CategoryResources.Reply.MeWithTaskGroup.from(category, filteredTaskGroups));
        }
        return filteredAllWithTaskGroup;
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

    public List<TaskRateResponse> calculateTaskRatesByDueDates(Long memberId, LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        List<TaskRateResponse> list = new ArrayList<>();
        for (int i = 0; i < period.getDays() + 1; i++) {
            double allTasks = taskDslRepository.findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(memberId, startDate.plusDays(i)).size();
            double doneTasks = taskDslRepository.findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(memberId, startDate.plusDays(i)).stream().filter(task -> task.getTaskStatus().equals(TaskStatus.COMPLETED)).count();
            double rate = 0;
            if (allTasks != 0) {
                rate = doneTasks / allTasks * 100.0;
            }
            TaskRateResponse response = new TaskRateResponse(startDate.plusDays(i), (int) rate);
            list.add(response);
        }
        return list;
    }

    public CategoryResources.Reply.MeWithTaskGroup deleteCategory(Long categoryId) {
        Category category = findById(categoryId);
        if (category.getType() == Category.CategoryType.DEFAULT) {
            throw new HomeControlException(CANNOT_DELETE_EXCEPTION);
        }
        category.expire();
        return CategoryResources.Reply.MeWithTaskGroup.from(category);
    }
}
