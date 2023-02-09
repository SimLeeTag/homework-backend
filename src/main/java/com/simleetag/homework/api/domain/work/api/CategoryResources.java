package com.simleetag.homework.api.domain.work.api;

import java.util.List;

import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.taskGroup.Cycle;
import com.simleetag.homework.api.domain.work.taskGroup.Difficulty;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupType;
import com.simleetag.homework.api.domain.work.taskGroup.api.TaskGroupResponse;

public record CategoryResources(
        Request request,
        Reply reply
) {
    public static class Request {
        public record Create(
                CategoryCreateRequest category,
                TaskGroupCreateRequest... taskGroups
        ) {
            public record CategoryCreateRequest(
                    Long categoryId,
                    String name
            ) {}

            public record TaskGroupCreateRequest(
                    Long taskGroupId,
                    String taskGroupName,
                    TaskGroupType taskGroupType,
                    Cycle cycle,
                    Difficulty difficulty,
                    Long point,
                    Long ownerId
            ) {}
        }
    }

    public static class Reply {
        public record MeWithTaskGroup(
                Long categoryId,
                String categoryName,
                Category.CategoryType categoryType,
                List<TaskGroupResponse> taskGroups
        ) {
            public static List<MeWithTaskGroup> from(List<Category> categories) {
                return categories.stream()
                                 .map(category -> new MeWithTaskGroup(
                                         category.getId(),
                                         category.getName(),
                                         category.getType(),
                                         TaskGroupResponse.from(category.getTaskGroups())))
                                 .toList();
            }

            public static MeWithTaskGroup from(Category category, List<TaskGroup> taskGroups) {
                return new MeWithTaskGroup(category.getId(), category.getName(), category.getType(), TaskGroupResponse.from(taskGroups));
            }

            public static MeWithTaskGroup from(Category category) {
                return new MeWithTaskGroup(category.getId(), category.getName(), category.getType(), TaskGroupResponse.from(category.getTaskGroups()));
            }

        }
    }
}
