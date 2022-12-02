package com.simleetag.homework.api.domain.work.api;

import java.util.List;
import javax.persistence.criteria.Join;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.CategoryService;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/categories")
public class CategoryMaintenanceController {
    private final CategoryService categoryService;

    @Operation(summary = "검색")
    @GetMapping
    public ResponseEntity<List<CategoryResources.Reply.MeWithTaskGroup>> search(@RequestParam(required = false) Long homeId,
                                                                                @RequestParam(required = false) Long categoryId) {
        var condition = new CategorySearchCondition(homeId, categoryId);
        final List<Category> categories = categoryService.search(condition);
        return ResponseEntity.ok(CategoryResources.Reply.MeWithTaskGroup.from(categories));
    }

    @Operation(summary = "등록")
    @PostMapping
    public ResponseEntity<Long> add(@RequestBody CategoryMaintenanceResources.Request.Category request) {
        return ResponseEntity.ok(categoryService.add(request).getId());
    }

    public record CategorySearchCondition(
            Long homeId,
            Long categoryId
    ) {
        private static Specification<Category> equalHomeId(Long homeId) {
            return (root, query, builder) -> {
                Join<Home, Category> categoryTask = root.join("home");
                return builder.equal(categoryTask.get("id"), homeId);
            };
        }

        private static Specification<Category> equalCategoryId(Long categoryId) {
            return (root, query, builder) -> builder.equal(root.get("id"), categoryId);
        }

        public Specification<Category> toSpecs() {
            Specification<Category> spec = (root, query, builder) -> null;
            if (homeId != null)
                spec = spec.and(equalHomeId(homeId));
            if (categoryId != null)
                spec = spec.and(equalCategoryId(categoryId));
            return spec;
        }
    }
}
