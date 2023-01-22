package com.simleetag.homework.api.domain.work.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.work.task.api.TaskEditRequest;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Task extends DeletableEntity {

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.UNFINISHED;

    @Column
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup taskGroup;

    public Task(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setBy(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        if (!taskGroup.getTasks().contains(this)) {
            taskGroup.addBy(this);
        }
    }

    public void setDueDate(LocalDate date) {
        this.dueDate = date;
    }

    public void changeStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }

    public void edit(TaskEditRequest request) {
        this.taskStatus = request.taskStatus();
        this.dueDate = request.dueDate();
    }
}
