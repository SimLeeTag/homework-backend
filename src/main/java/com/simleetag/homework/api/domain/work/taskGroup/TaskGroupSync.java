package com.simleetag.homework.api.domain.work.taskGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberFinder;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.api.CategoryResources;

import org.apache.commons.lang3.StringUtils;

public class TaskGroupSync {
    private final TaskGroupRepository taskGroupRepository;

    private final MemberFinder memberFinder;

    private final List<Category> categories;

    private final List<CategoryResources.Request.Create> requests;

    public TaskGroupSync(TaskGroupRepository taskGroupRepository, MemberFinder memberFinder, List<Category> categories, List<CategoryResources.Request.Create> requests) {
        this.taskGroupRepository = taskGroupRepository;
        this.memberFinder = memberFinder;
        this.categories = categories;
        this.requests = requests;
    }

    public void sync() {
        var modifiedAt = LocalDateTime.now();
        for (CategoryResources.Request.Create request : requests) {
            final var categoryId = request.category().categoryId();
            for (CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupRequest : request.taskGroup()) {
                final var taskGroupId = taskGroupRequest.taskGroupId();
                Member owner = taskGroupRequest.ownerId() == null ? null : memberFinder.findByIdOrElseThrow(taskGroupRequest.ownerId());
                TaskGroup taskGroup = new TaskGroup();
                if (Objects.nonNull(categoryId)) {

                    // 태스크 그룹 Upsert
                    final Category foundCategory = findCategoryByIdOrElseThrow(categoryId);

                    if (Objects.nonNull(taskGroupId)) {
                        label:
                        for (Category category : categories) {
                            for (TaskGroup group : category.getTaskGroups()) {
                                if (group.getId().equals(taskGroupId)) {
                                    taskGroup = group;
                                    break label;
                                }
                            }
                        }
                    }

                    taskGroup.sync(taskGroupRequest, owner);
                    taskGroup.setBy(foundCategory);
                    taskGroupRepository.save(taskGroup);
                    continue;
                }

                if (Objects.isNull(taskGroupId)) {
                    // 만약 TaskGroupId와 CategoryId가 null이고, TaskGroupName이 null이 아니면 태스크 그룹을 추가한다.
                    if (!StringUtils.isBlank(taskGroupRequest.taskGroupName())) {
                        final Category foundCategory = findCategoryByNameOrElseThrow(request);

                        taskGroup.sync(taskGroupRequest, owner);
                        taskGroup.setBy(foundCategory);
                        taskGroupRepository.save(taskGroup);
                    }
                }
            }
        }

        for (Category category : categories) {
            for (TaskGroup taskGroup : category.getTaskGroups()) {
                if (taskGroup.getModifiedAt().isBefore(modifiedAt)) {
                    taskGroup.expire();
                }
            }
        }
    }

    private Category findCategoryByNameOrElseThrow(CategoryResources.Request.Create request) {
        return categories.stream()
                         .filter(category -> request.category().name().equals(category.getName()))
                         .findFirst()
                         .orElseThrow(() -> new IllegalArgumentException("DB에 저장된 카테고리가 요청 카테고리를 포함하고 있지 않습니다."));
    }

    private Category findCategoryByIdOrElseThrow(Long categoryId) {
        return categories.stream()
                         .filter(category -> categoryId.equals(category.getId()))
                         .findFirst()
                         .orElseThrow(() -> new IllegalArgumentException("DB에 저장된 카테고리가 요청 카테고리를 포함하고 있지 않습니다."));
    }
}
