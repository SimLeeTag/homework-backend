package com.simleetag.homework.api.domain.task;

import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Task extends DeletableEntity {

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup taskGroup;

    @Column
    private Long assigneeId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private RequestStatus requestStatus = RequestStatus.NONE;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.UNFINISHED;

    @Column
    private boolean deleted = false;

    public Task(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
