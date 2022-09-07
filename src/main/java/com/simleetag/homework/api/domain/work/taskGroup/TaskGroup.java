package com.simleetag.homework.api.domain.work.taskGroup;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.task.Task;
import com.simleetag.homework.api.domain.work.task.TaskLevel;
import com.simleetag.homework.utils.JsonMapper;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class TaskGroup extends DeletableEntity {

    @OneToMany(mappedBy = "taskGroup")
    private final List<Task> tasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private final boolean deleted = false;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskLevel level;

    @Column
    private String name;

    @Column
    private Long point;

    @Column
    private String textOfPeriod;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskGroupType type;

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

    public void addBy(Task task) {
        this.tasks.add(task);
        if (task.getTaskGroup() != this) {
            task.setBy(this);
        }
    }

    public Period getPeriod() {
        if (!StringUtils.hasText(textOfPeriod)) {
            return null;
        }

        return JsonMapper.readValue(textOfPeriod, Period.class);
    }

    public void setPeriod(Period period) {
        this.textOfPeriod = JsonMapper.writeValueAsString(period);
    }
}
