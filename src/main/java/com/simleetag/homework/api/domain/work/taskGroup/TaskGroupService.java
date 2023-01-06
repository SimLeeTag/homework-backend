package com.simleetag.homework.api.domain.work.taskGroup;


import java.util.List;

import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.home.member.MemberFinder;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupMaintenanceResources;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskGroupService {
    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID 에 해당하는 집안일 꾸러미가 존재하지 않습니다.";

    private final TaskGroupRepository taskGroupRepository;

    private final MemberFinder memberFinder;

    public TaskGroup findById(Long categoryId, Long taskGroupId) {
        final TaskGroup taskGroup = findById(taskGroupId);
        if (!categoryId.equals(taskGroup.getCategory().getId())) {
            throw new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, taskGroupId));
        }

        return taskGroup;
    }

    public TaskGroup findById(Long id) {
        return taskGroupRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, id)));
    }

    public TaskGroup add(Category category, TaskGroupMaintenanceResources.Request.TaskGroup request) {
        Member owner = request.ownerId() == null ? null : memberFinder.findByIdOrElseThrow(request.ownerId());
        final TaskGroup taskGroup = new TaskGroup(
                request.difficulty(),
                request.name(),
                request.point(),
                request.type(),
                request.cycle());
        taskGroup.setCategoryBy(category);
        if (owner != null) {
            taskGroup.setOwnerBy(owner);
        }
        return taskGroupRepository.save(taskGroup);
    }

    public void sync(List<CategoryResources.Request.Create> requests, List<Category> categories) {
        new TaskGroupSync(taskGroupRepository, memberFinder, categories, requests).sync();
    }
}
