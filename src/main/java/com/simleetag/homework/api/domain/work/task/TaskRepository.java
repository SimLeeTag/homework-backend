package com.simleetag.homework.api.domain.work.task;

import java.time.LocalDate;
import java.util.List;

import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByTaskGroup(TaskGroup taskGroup);

}
