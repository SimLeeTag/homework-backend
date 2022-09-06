package com.simleetag.homework.api.domain.task;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Category extends DeletableEntity {

    @OneToMany(mappedBy = "category")
    private final List<TaskGroup> taskGroups = new ArrayList<>();

    @Column
    private Long homeId;

    @Column
    private String name;

    @Column
    private boolean deleted = false;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CategoryType type;

    public Category(Long homeId, String name, CategoryType type) {
        this.homeId = homeId;
        this.name = name;
        this.type = type;
    }

    public void addBy(TaskGroup taskGroup) {
        this.taskGroups.add(taskGroup);
        if (taskGroup.getCategory() != this) {
            taskGroup.setBy(this);
        }
    }
}
