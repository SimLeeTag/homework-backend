package com.simleetag.homework.api.domain.work.api;

import java.util.List;

import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.domain.work.Category;
import com.simleetag.homework.api.domain.work.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "집안일 조회",
            description = """
                    해당 집에 등록된 집안일을 조회합니다.
                    처음에는 기본 집안일이 등록되어 있습니다.
                    """
    )
    @GetMapping
    public ResponseEntity<List<CategoryResources.Reply.MeWithTaskGroup>> findAllWithTaskGroup(@Invitation Long homeId) {
        final List<Category> categories = categoryService.findAllWithTaskGroupByHomeId(homeId);
        return ResponseEntity.ok(CategoryResources.Reply.MeWithTaskGroup.from(categories));
    }

    @Operation(
            summary = "집안일 등록",
            description = """
                    집안일을 등록합니다.
                    변경이 없는 카테고리와 집안일이어도 반드시 해당 카테고리와 집안일의 ID를 같이 보내야합니다.
                                        
                    - Request의 카테고리ID가 null이 아니면서,
                      - DB에 해당 카테고리 ID가 존재할 경우, 해당 카테고리는 Request의 정보로 업데이트됩니다.
                                        
                    - Request의 카테고리가 null이면서,
                      - CategoryName이 null이 아닐 경우 해당 카테고리를 DB에 추가합니다.
                                        
                    - 그 외 DB에 저장되어있던 변경이 없는 카테고리는 모두 삭제됩니다.                                       
                    
                    집안일꾸러미도 같은 로직으로 적용됩니다.
                    """
    )
    @PostMapping
    public ResponseEntity<Void> sync(@Invitation Long homeId,
                                     @RequestBody List<CategoryResources.Request.Create> requests) {
        categoryService.sync(homeId, requests);
        return ResponseEntity.ok().build();
    }
}
