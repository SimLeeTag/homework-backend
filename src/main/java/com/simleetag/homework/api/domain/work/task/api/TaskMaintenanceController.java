package com.simleetag.homework.api.domain.work.task.api;

import java.util.List;

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

    @Operation(summary = "편집")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> edit(@RequestBody TaskEditRequest request,
                                             @PathVariable Long categoryId,
                                             @PathVariable Long taskGroupId,
                                             @PathVariable Long taskId) {
        return ResponseEntity.ok(TaskResponse.from(taskService.edit(taskId, request)));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> searchOne(@PathVariable Long categoryId,
                                                  @PathVariable Long taskGroupId,
                                                  @PathVariable Long taskId) {
        return ResponseEntity.ok(TaskResponse.from(taskService.findById(taskId)));
    }

    @Operation(summary = "TaskGroup으로 전체 조회")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> searchByTaskGroup(@PathVariable Long categoryId,
                                                                @PathVariable Long taskGroupId) {
        return ResponseEntity.ok(TaskResponse.from(taskService.searchAllByTaskGroup(taskGroupId)));
    }

}
