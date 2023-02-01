package com.simleetag.homework.api.domain.work.taskGroup;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    Optional<TaskGroup> findByIdAndDeletedAtIsNull(Long taskId);
}
