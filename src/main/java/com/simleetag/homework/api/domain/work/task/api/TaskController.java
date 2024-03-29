package com.simleetag.homework.api.domain.work.task.api;

import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.domain.work.task.Task;
import com.simleetag.homework.api.domain.work.task.TaskService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집안일")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "집안일 완료 여부 변경",
            description = """
                    해당 집안일 수행을 완료하거나 취소합니다.
                    """
    )
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> changeTaskStatus(@Invitation Long homeId,
                                                         @PathVariable @Positive Long taskId,
                                                         @RequestBody TaskStatusEditRequest request) {
        final Task task = taskService.changeStatus(taskId, request);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

    @Operation(
            summary = "집안일 마감일 변경",
            description = """
                    해당 집안일 마감일을 변경합니다.
                    """
    )
    @PatchMapping("/{taskId}/change-duedate")
    public ResponseEntity<TaskResponse> changeTaskDueDate(@Invitation Long homeId,
                                                          @PathVariable @Positive Long taskId,
                                                          @RequestBody TaskDueDateEditRequest request) {
        final Task task = taskService.changeDueDate(taskId, request);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

    @Operation(
            summary = "집안일 삭제",
            description = """
                    해당 집안일을 삭제합니다.
                    """
    )
    @DeleteMapping("{taskId}")
    public ResponseEntity<TaskResponse> deleteTask(@Invitation Long homeId,
                                                   @PathVariable @Positive Long taskId) {
        return ResponseEntity.ok(TaskResponse.from(taskService.delete(taskId)));
    }

    @Operation(
            summary = "집안일 상세 조회",
            description = """
                    해당 집안일을 조회합니다.
                    """
    )
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> findOne(@Invitation Long homeId,
                                                @PathVariable @Positive Long taskId) {
        final Task task = taskService.findById(taskId);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

}
