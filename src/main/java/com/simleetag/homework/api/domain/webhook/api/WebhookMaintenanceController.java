package com.simleetag.homework.api.domain.webhook.api;

import com.simleetag.homework.api.domain.webhook.Slack;
import com.simleetag.homework.api.domain.webhook.SlackFinder;
import com.simleetag.homework.api.domain.webhook.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "웹훅 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance/webhook")
public class WebhookMaintenanceController {
    private final SlackFinder slackFinder;
    private final SlackService slackService;

    @Operation(summary = "슬랙 채널 조회")
    @GetMapping("/slacks/{id}")
    public ResponseEntity<WebhookMaintenanceResources.Reply.SlackChannel> find(@PathVariable Long id) {
        Slack slack = slackFinder.findById(id);
        var reply = WebhookMaintenanceResources.Reply.SlackChannel.from(slack);
        return ResponseEntity.ok(reply);
    }

    @Operation(summary = "슬랙 채널 등록")
    @PostMapping("/slacks")
    public ResponseEntity<Void> add(@RequestBody WebhookMaintenanceResources.Request.Slack request) {
        slackService.add(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "슬랙 채널 삭제")
    @DeleteMapping("/slacks/{ids}")
    public ResponseEntity<Void> expireAll(@PathVariable List<Long> ids) {
        slackService.expireAll(ids);
        return ResponseEntity.ok().build();
    }
}
