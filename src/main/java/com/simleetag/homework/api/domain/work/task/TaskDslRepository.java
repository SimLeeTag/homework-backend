package com.simleetag.homework.api.domain.work.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.simleetag.homework.api.domain.work.task.QTask.task;
import static com.simleetag.homework.api.domain.work.taskGroup.QTaskGroup.taskGroup;

@Repository
public class TaskDslRepository extends QuerydslRepositorySupport {

    public TaskDslRepository() {super(Task.class);}

    public List<Task> findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(Long ownerId, LocalDate date) {
        return from(task)
                .innerJoin(task.taskGroup, taskGroup).fetchJoin()
                .where(
                        task.taskGroup.owner.id.eq(ownerId)
                                               .and(task.dueDate.eq(date))
                                               .and(task.deletedAt.isNull())
                ).fetch();
    }
}
