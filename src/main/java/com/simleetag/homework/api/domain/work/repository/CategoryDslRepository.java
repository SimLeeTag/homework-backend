package com.simleetag.homework.api.domain.work.repository;

import java.util.List;

import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.work.Category;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.simleetag.homework.api.domain.home.QHome.home;
import static com.simleetag.homework.api.domain.work.QCategory.category;
import static com.simleetag.homework.api.domain.work.taskGroup.QTaskGroup.taskGroup;

@Repository
public class CategoryDslRepository extends QuerydslRepositorySupport {
    public CategoryDslRepository() {
        super(User.class);
    }

    public List<Category> findAllWithTaskGroupByHomeId(Long homeId) {
        return from(category)
                .distinct()
                .innerJoin(category.taskGroups, taskGroup).fetchJoin()
                .innerJoin(category.home, home).fetchJoin()
                .where(
                        category.home.id.eq(homeId)
                                        .and(category.deletedAt.isNull())
                                        .and(taskGroup.deletedAt.isNull())
                ).fetch();
    }
}
