package com.simleetag.homework.api.domain.work.task;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTaskGroupAndDeletedAtIsNull(TaskGroup taskGroup);

    Optional<Task> findByIdAndDeletedAtIsNull(Long taskId);

}
