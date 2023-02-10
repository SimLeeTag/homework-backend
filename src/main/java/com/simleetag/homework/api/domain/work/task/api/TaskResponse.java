package com.simleetag.homework.api.domain.work.task.api;

import java.time.LocalDate;
import java.util.List;

import com.simleetag.homework.api.domain.work.task.Task;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

public record TaskResponse(
        Long taskId,
        String taskName,
        TaskGroupType taskType,
        Difficulty difficulty,
        Long point,
        TaskStatus taskStatus,
        LocalDate dueDate
) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTaskGroup().getName(),
                task.getTaskGroup().getType(),
                task.getTaskGroup().getDifficulty(),
                task.getTaskGroup().getPoint(),
                task.getTaskStatus(),
                task.getDueDate()
        );
    }

    public static List<TaskResponse> from(List<Task> tasks) {
        return tasks.stream()
                    .map(task -> new TaskResponse(
                            task.getId(),
                            task.getTaskGroup().getName(),
                            task.getTaskGroup().getType(),
                            task.getTaskGroup().getDifficulty(),
                            task.getTaskGroup().getPoint(),
                            task.getTaskStatus(),
                            task.getDueDate()
                    ))
                    .toList();
    }

}
