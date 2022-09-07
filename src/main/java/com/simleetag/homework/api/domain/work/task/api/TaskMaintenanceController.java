package com.simleetag.homework.api.domain.work.task.api;

import com.simleetag.homework.api.domain.work.task.TaskService;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집안일 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/categories/{categoryId}/task-groups/{taskGroupId}/tasks")
public class TaskMaintenanceController {
    private final TaskGroupService taskGroupService;
    private final TaskService taskService;

    @Operation(summary = "등록")
    @PostMapping
    public ResponseEntity<Long> add(@RequestBody TaskCreateRequest request,
                                    @PathVariable Long categoryId,
                                    @PathVariable Long taskGroupId) {
        final TaskGroup taskGroup = taskGroupService.findById(categoryId, taskGroupId);
        return ResponseEntity.ok(taskService.add(taskGroup, request).getId());
    }
}
