package com.simleetag.homework.api.domain.work.task;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTaskGroupIdAndDeletedAtIsNull(Long taskGroupId);

    Optional<Task> findByIdAndDeletedAtIsNull(Long taskId);

}
