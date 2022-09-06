package com.simleetag.homework.api.domain.task.api;

import com.simleetag.homework.api.domain.task.Category;
import com.simleetag.homework.api.domain.task.CategoryService;
import com.simleetag.homework.api.domain.task.TaskGroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집안일 꾸러미 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/categories/{categoryId}")
public class TaskGroupMaintenanceController {
    private final TaskGroupService taskGroupService;
    private final CategoryService categoryService;

    @Operation(summary = "등록")
    @PostMapping
    public ResponseEntity<Long> add(@RequestBody TaskGroupCreateRequest request,
                                    @PathVariable Long categoryId) {
        final Category category = categoryService.findById(categoryId);
        return ResponseEntity.ok(taskGroupService.add(category, request).getId());
    }
}
