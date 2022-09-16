package com.simleetag.homework.api.domain.work.task;

import java.time.LocalDate;
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
    @Enumerated(value = EnumType.STRING)
    private final RequestStatus requestStatus = RequestStatus.NONE;

    @Column
    @Enumerated(value = EnumType.STRING)
    private final TaskStatus taskStatus = TaskStatus.UNFINISHED;

    @Column
    private LocalDate dueDate;

    /**
     * 집안일 별로 난이도를 가져야 한다.
     * 집안일 꾸러미가 난이도를 가질 경우 일회성 집안일들은 모두 같은 난이도를 갖게 되어버리기 때문이다.
     */
    @Column
    @Enumerated(value = EnumType.STRING)
    private Difficulty level;

    @Column
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup taskGroup;

    public Task(Long ownerId) {
        this.ownerId = ownerId;
        this.dueDate = LocalDate.now();
    }

    public Task(Long ownerId, LocalDate dueDate) {
        this.ownerId = ownerId;
        this.dueDate = dueDate;
    }

    public void setBy(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        if (!taskGroup.getTasks().contains(this)) {
            taskGroup.addBy(this);
        }
    }
}
