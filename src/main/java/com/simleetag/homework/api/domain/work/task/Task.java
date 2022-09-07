package com.simleetag.homework.api.domain.work.task;

import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Task extends DeletableEntity {

    @Column
    private Long assigneeId;

    @Column
    private final boolean deleted = false;

    @Column
    @Enumerated(value = EnumType.STRING)
    private final RequestStatus requestStatus = RequestStatus.NONE;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup taskGroup;

    @Column
    @Enumerated(value = EnumType.STRING)
    private final TaskStatus taskStatus = TaskStatus.UNFINISHED;

    public Task(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setBy(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        if (!taskGroup.getTasks().contains(this)) {
            taskGroup.addBy(this);
        }
    }
}
