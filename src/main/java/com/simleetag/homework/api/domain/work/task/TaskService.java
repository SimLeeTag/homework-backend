package com.simleetag.homework.api.domain.work.task;

import com.simleetag.homework.api.domain.work.task.api.TaskCreateRequest;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task add(TaskGroup taskGroup, TaskCreateRequest request) {
        final Task task = new Task();
        task.setBy(taskGroup);
        return taskRepository.save(task);
    }
}
