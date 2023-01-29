package com.simleetag.homework.api.domain.work.taskGroup.api;

import javax.validation.constraints.Positive;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroup;
import com.simleetag.homework.api.domain.work.taskGroup.TaskGroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "집안일 꾸러미")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task-groups")
public class TaskGroupController {

    private final TaskGroupService taskGroupService;

    @Operation(
            summary = "집안일 수정",
            description = """
                    해당 잡인일의 이름, 난이도, 담당자, 주기를 변경합니다.
                    """
    )
    @PutMapping("/{taskGroupId}")
    public ResponseEntity<TaskGroupResponse> editTaskGroup(@Invitation Long homeId,
                                                           @PathVariable @Positive Long taskGroupId,
                                                           @RequestBody TaskGroupEditRequest request) {
        TaskGroup taskGroup = taskGroupService.edit(taskGroupId, request);

        return ResponseEntity.ok(TaskGroupResponse.from(taskGroup));
    }
}
