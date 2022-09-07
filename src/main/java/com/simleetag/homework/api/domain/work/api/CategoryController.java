package com.simleetag.homework.api.domain.work.api;

import java.util.List;

import com.simleetag.homework.api.domain.work.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "기본 제공 집안일 조회")
    @GetMapping("default")
    public ResponseEntity<List<CategoryWithDefaultTaskResponse>> findAllDefaultTask() {
        return ResponseEntity.ok(categoryService.findAllDefaultCategoryWithTask());
    }
}
