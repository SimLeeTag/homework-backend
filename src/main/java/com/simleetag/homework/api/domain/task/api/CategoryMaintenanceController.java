package com.simleetag.homework.api.domain.task.api;

import com.simleetag.homework.api.domain.task.CategoryService;

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

    @Operation(summary = "등록")
    @GetMapping
    public ResponseEntity<Long> add(@RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.add(request).getId());
    }

    @Operation(summary = "기본 제공 카테고리 및 집안일 꾸러미 등록")
    @PostMapping
    public ResponseEntity<Void> addAllDefaultCategoryWithTaskGroup(@RequestBody DefaultCategoryWithTaskCreateRequest request) {
        categoryService.addAllDefaultCategoryWithTaskGroup(request);
        return ResponseEntity.ok().build();
    }
}
