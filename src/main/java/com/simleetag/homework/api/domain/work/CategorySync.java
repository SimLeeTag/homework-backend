package com.simleetag.homework.api.domain.work;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.work.api.CategoryResources;
import com.simleetag.homework.api.domain.work.repository.CategoryRepository;

import org.apache.commons.lang3.StringUtils;

public class CategorySync {
    private final CategoryRepository categoryRepository;

    private final List<CategoryResources.Request.Create> requests;

    private final List<Category> categories;

    public CategorySync(CategoryRepository categoryRepository, List<CategoryResources.Request.Create> requests, List<Category> categories) {
        this.categoryRepository = categoryRepository;
        this.requests = requests;
        this.categories = categories;
    }

    public void sync(Home home) {
        var modifiedAt = LocalDateTime.now();
        for (CategoryResources.Request.Create request : requests) {
            final var categoryRequest = request.category();
            final var categoryId = categoryRequest.categoryId();

            // 카테고리 정보 업데이트
            if (Objects.nonNull(categoryId)) {
                final Category category = findByIdOrElseThrow(categoryId);
                category.sync(categoryRequest);
                continue;
            }

            // 카테고리 추가
            if (!StringUtils.isBlank(categoryRequest.name())) {
                final Category category = categoryRepository.save(new Category(home, modifiedAt, categoryRequest.name(), Category.CategoryType.ROUTINE));
                categories.add(category);
            }
        }

        // 카테고리 삭제
        for (Category category : categories) {
            if (category.getModifiedAt().isBefore(modifiedAt)) {
                category.expire();
            }
        }
    }

    private Category findByIdOrElseThrow(Long categoryId) {
        return categories.stream()
                         .filter(category -> categoryId.equals(category.getId()))
                         .findAny()
                         .orElseThrow(() -> new IllegalArgumentException(String.format("[%d] ID를 가진 카테고리가 존재하지 않습니다.", categoryId)));
    }
}
