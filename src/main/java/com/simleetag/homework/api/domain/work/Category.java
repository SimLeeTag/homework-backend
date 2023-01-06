package com.simleetag.homework.api.domain.work;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category extends DeletableEntity {

    @OneToMany(mappedBy = "category")
    private final List<TaskGroup> taskGroups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column
    private String name;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CategoryType type;

    public Category(String name, CategoryType type, Home home) {
        this.name = name;
        this.type = type;
        this.home = home;
    }

    public void addBy(TaskGroup taskGroup) {
        this.taskGroups.add(taskGroup);
        if (taskGroup.getCategory() != this) {
            taskGroup.setCategoryBy(this);
        }
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }

    public void sync(CategoryResources.Request.Create.CategoryCreateRequest categoryRequest) {
        this.name = categoryRequest.name();
        this.modifiedAt = LocalDateTime.now();
    }

    public enum CategoryType {
        DEFAULT, ROUTINE, TEMPORARY
    }
}
