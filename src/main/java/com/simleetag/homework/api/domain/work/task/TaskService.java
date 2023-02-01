package com.simleetag.homework.api.domain.work.task;

import java.time.LocalDate;
import java.util.List;

import com.simleetag.homework.api.common.exception.HomeControlException;
import com.simleetag.homework.api.domain.work.task.api.TaskCreateRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskDueDateEditRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskEditRequest;
import com.simleetag.homework.api.domain.work.task.api.TaskStatusEditRequest;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private static final String ENTITY_NOT_FOUND_EXCEPTION = "[%d] ID에 해당하는 집안일이 존재하지 않습니다.";

    private static final String CANNOT_EDIT_EXCEPTION = "해당 집안일은 수정이 불가능합니다.";

    private final TaskRepository taskRepository;

    public Task add(TaskGroup taskGroup, TaskCreateRequest request) {
        final Task task = new Task();
        task.setBy(taskGroup);
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findByIdAndDeletedAtIsNull(id)
                             .orElseThrow(() -> new IllegalArgumentException(String.format(ENTITY_NOT_FOUND_EXCEPTION, id)));
    }

    public Task edit(Long taskId, TaskEditRequest request) {
        final Task task = findById(taskId);
        task.edit(request);
        return task;
    }

    public Task changeStatus(Long taskId, TaskStatusEditRequest request) {
         final Task task = findById(taskId);
        task.changeStatus(request.taskStatus());
        return task;
    }

    public Task changeDueDate(Long taskId, TaskDueDateEditRequest request) {
        final Task task = findById(taskId);
        validateEditable(task, request);
        task.setDueDate(request.dueDate());
        return task;
    }

    private void validateEditable(Task task, TaskDueDateEditRequest request) {
        boolean valid = true;
        if (task.getTaskStatus().equals(TaskStatus.COMPLETED) || request.dueDate().isBefore(LocalDate.now())) {
            valid = false;
        }
        if (!valid) {
            throw new HomeControlException(CANNOT_EDIT_EXCEPTION);
        }
    }

    public List<Task> searchAllByTaskGroup(TaskGroup taskGroup) {
        return taskRepository.findByTaskGroupAndDeletedAtIsNull(taskGroup);
    }

    public List<Task> searchAll() {
        return taskRepository.findAll();
    }

    public Task delete(Long taskId) {
        final Task task = findById(taskId);
        task.expire();
        return task;
    }
}
