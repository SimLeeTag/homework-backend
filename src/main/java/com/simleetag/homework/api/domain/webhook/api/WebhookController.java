package com.simleetag.homework.api.domain.webhook.api;

import com.simleetag.homework.api.domain.webhook.PinpointMessageBrokerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹훅")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {
    private final PinpointMessageBrokerService pinpointMessageBrokerService;

    @Operation(
            summary = "PINPOINT -> Slack 웹훅",
            description = "PINPOINT로부터 들어온 웹훅을 슬랙으로 전달한다."
    )
    @PostMapping("/from/pinpoint/to/slack")
    public ResponseEntity<Void> pinpointToSlack(@RequestBody PinpointResources.Payload request) {
        pinpointMessageBrokerService.toSlack(request);
        return ResponseEntity.ok().build();
    }
}
