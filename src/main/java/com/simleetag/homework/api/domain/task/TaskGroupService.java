package com.simleetag.homework.api.domain.task;

import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.domain.task.api.DefaultTaskGroupCreateRequest;
import com.simleetag.homework.api.domain.task.api.TaskGroupCreateRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;

    public TaskGroup add(Category category, TaskGroupCreateRequest request) {
        final TaskGroup taskGroup = new TaskGroup(request.name(), request.type());
        taskGroup.setBy(category);
        return taskGroupRepository.save(taskGroup);
    }

    public void addAllDefaultTaskGroup(Category category, TaskGroupCreateRequest... requests) {
        Arrays.stream(requests).forEach(request -> add(category, request));
    }
}
