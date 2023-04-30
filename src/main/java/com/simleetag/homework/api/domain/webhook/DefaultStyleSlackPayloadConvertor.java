package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.domain.webhook.api.PinpointResources;

import java.time.LocalDateTime;
import java.util.List;

public record DefaultStyleSlackPayloadConvertor(
        PinpointResources.Payload request
) implements SlackPayloadConvertor {

    @Override
    public SlackResources.Payload pinpointToSlack(PinpointResources.Payload payload) {
        var section = String.format("[Emergency]: %s가 %d%%를 초과했습니다. 현재 사용률: %d%%\n",
                payload.checker().name(),
                payload.threshold(),
                payload.checker().detectedValue().get(0).agentValue());

        var context = String.format("[Application]: %s | %s", payload.applicationId(), LocalDateTime.now());
        return new SlackResources.Payload(
                List.of(
                        new BlockKit.Section(
                                new BlockKit.Text(
                                        BlockKit.Text.Type.mrkdwn,
                                        section
                                )
                        ),
                        new BlockKit.Context(
                                List.of(
                                        new BlockKit.Text(
                                                BlockKit.Text.Type.mrkdwn,
                                                context
                                        )
                                )
                        )
                )
        );
    }
}
