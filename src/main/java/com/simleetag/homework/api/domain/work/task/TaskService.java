package com.simleetag.homework.api.domain.work.task;

import java.util.List;

import com.simleetag.homework.api.domain.work.task.api.TaskCreateRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskEditRequest;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID 에 해당하는 집안일이 존재하지 않습니다.";

    private final TaskRepository taskRepository;

    public Task add(TaskGroup taskGroup, TaskCreateRequest request) {
        final Task task = new Task();
        task.setBy(taskGroup);
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, id)));
    }

    public Task edit(Long taskId, TaskEditRequest request) {
        Task task = findById(taskId);
        task.edit(request);
        return task;
    }

    public List<Task> searchAllByTaskGroup(TaskGroup taskGroup) {
        return taskRepository.findByTaskGroup(taskGroup);
    }

    public List<Task> searchAll() {
        return taskRepository.findAll();
    }
}
