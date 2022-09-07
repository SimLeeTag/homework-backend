package com.simleetag.homework.api.domain.work.taskGroup;

import java.util.Arrays;

import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.api.TaskGroupCreateRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskGroupService {
    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID 에 해당하는 집안일 꾸러미가 존재하지 않습니다.";

    private final TaskGroupRepository taskGroupRepository;

    public TaskGroup add(Category category, TaskGroupCreateRequest request) {
        final TaskGroup taskGroup = new TaskGroup(request.name(), request.type());
        taskGroup.setBy(category);
        return taskGroupRepository.save(taskGroup);
    }

    public void addAllDefaultTaskGroup(Category category, TaskGroupCreateRequest... requests) {
        Arrays.stream(requests).forEach(request -> add(category, request));
    }

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
}
