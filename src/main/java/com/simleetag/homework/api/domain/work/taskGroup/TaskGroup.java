package com.simleetag.homework.api.domain.work.taskGroup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.task.Task;
import com.simleetag.homework.utils.JsonMapper;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskGroup extends DeletableEntity {

    @OneToMany(mappedBy = "taskGroup", cascade = CascadeType.ALL)
    private final List<Task> tasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Column
    private Long point;

    @Column(columnDefinition = "TEXT")
    private String textOfCycle;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskGroupType type;

    public TaskGroup(String name, TaskGroupType type) {
        this(name, type, null);
    }

    public TaskGroup(String name, TaskGroupType type, LocalDateTime modifiedAt) {
        this(null, name, null, null, type, null);
        this.modifiedAt = modifiedAt;
    }

    public TaskGroup(Difficulty difficulty, String name, Member owner, Long point, TaskGroupType type, Cycle cycle) {
        this.name = name;
        this.type = type;
        this.difficulty = difficulty;
        this.owner = owner;
        this.point = point;
        setCycle(cycle);
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

    public Cycle getCycle() {
        if (Objects.isNull(this.textOfCycle)) {
            return new Cycle(new ArrayList<>(), null, null);
        }
        return JsonMapper.readValue(textOfCycle, Cycle.class);
    }

    public void setCycle(Cycle cycle) {
        if (Objects.isNull(cycle)) {
            cycle = new Cycle(new ArrayList<>(), null, null);
        }

        this.textOfCycle = JsonMapper.writeValueAsString(cycle);
    }

    public void sync(CategoryResources.Request.Create.TaskGroupCreateRequest taskGroupRequest, Member owner) {
        this.name = taskGroupRequest.taskGroupName();
        this.difficulty = taskGroupRequest.difficulty();
        this.point = taskGroupRequest.point();
        this.owner = owner;
        this.type = taskGroupRequest.taskGroupType();
        this.modifiedAt = LocalDateTime.now();
        syncCycle(taskGroupRequest.cycle());
    }

    public void syncCycle(Cycle cycle) {
        for (DayOfWeek dayOfWeekOfRequest : cycle.dayOfWeeks()) {
            if (!getCycle().dayOfWeeks().contains(dayOfWeekOfRequest)) {
                final LocalDate dueDate = LocalDate.now().with(WeekFields.of(Locale.KOREA).dayOfWeek(), 1);
                addBy(new Task(dueDate));
            }
        }

        for (DayOfWeek dayOfWeekOfThis : getCycle().dayOfWeeks()) {
            if (!cycle.dayOfWeeks().contains(dayOfWeekOfThis)) {
                tasks.stream()
                     .filter(task -> task.getDueDate().getDayOfWeek().equals(dayOfWeekOfThis))
                     .findAny()
                     .ifPresent(Task::expire);
            }
        }
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }
}
