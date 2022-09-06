package com.simleetag.homework.api.domain.task;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class TaskGroup extends DeletableEntity {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String name;

    @ElementCollection
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();

    private int term;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskGroupType type;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskLevel level;

    @Column
    private Long point;

    @Column
    private boolean deleted = false;

    public TaskGroup(String name, TaskGroupType type) {
        this.name = name;
        this.type = type;
    }

    public void setBy(Category category) {
        this.category = category;
        if (!category.getTaskGroups().contains(this)) {
            category.addBy(this);
        }
    }
}
