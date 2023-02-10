package com.simleetag.homework.api.domain.work;

import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.work.repository.CategoryRepository;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupRepository;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;

public class DefaultCategory {
    private final CategoryRepository categoryRepository;

    private final TaskGroupRepository taskGroupRepository;

    public DefaultCategory(CategoryRepository categoryRepository, TaskGroupRepository taskGroupRepository) {
        this.categoryRepository = categoryRepository;
        this.taskGroupRepository = taskGroupRepository;
    }

    public void setDefaultCategoriesAtHome(Home home) {
        final List<TaskGroup> cookTaskGroups = Arrays.asList(
                new TaskGroup("아침 식사 준비", TaskGroupType.ROUTINE),
                new TaskGroup("점심 식사 준비", TaskGroupType.ROUTINE),
                new TaskGroup("저녁 식사 준비", TaskGroupType.ROUTINE),
                new TaskGroup("설거지", TaskGroupType.ROUTINE),
                new TaskGroup("장보기", TaskGroupType.ROUTINE),
                new TaskGroup("냉장고 정리", TaskGroupType.ROUTINE)
        );

        final Category cookCategory = new Category("🍚 요리", Category.CategoryType.DEFAULT, home);
        cookTaskGroups.forEach(cookCategory::addBy);
        taskGroupRepository.saveAll(cookTaskGroups);
        categoryRepository.save(cookCategory);

        final List<TaskGroup> cleanTaskGroups = Arrays.asList(
                new TaskGroup("분리수거", TaskGroupType.ROUTINE),
                new TaskGroup("음식물 쓰레기 버리기", TaskGroupType.ROUTINE),
                new TaskGroup("청소기 돌리기", TaskGroupType.ROUTINE),
                new TaskGroup("물걸레질", TaskGroupType.ROUTINE),
                new TaskGroup("화장실 청소", TaskGroupType.ROUTINE)
        );

        final Category cleanCategory = new Category("✨ 청소", Category.CategoryType.DEFAULT, home);
        cleanTaskGroups.forEach(cleanCategory::addBy);
        taskGroupRepository.saveAll(cleanTaskGroups);
        categoryRepository.save(cleanCategory);

        final List<TaskGroup> laundryTaskGroups = Arrays.asList(
                new TaskGroup("빨랫감 수거", TaskGroupType.ROUTINE),
                new TaskGroup("빨래 돌리기", TaskGroupType.ROUTINE),
                new TaskGroup("빨래 널기", TaskGroupType.ROUTINE),
                new TaskGroup("건조대 널기", TaskGroupType.ROUTINE),
                new TaskGroup("빨래 개기", TaskGroupType.ROUTINE)
        );

        final Category laundryCategory = new Category("👔 빨래", Category.CategoryType.DEFAULT, home);
        laundryTaskGroups.forEach(laundryCategory::addBy);
        taskGroupRepository.saveAll(laundryTaskGroups);
        categoryRepository.save(laundryCategory);
    }
}
