package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.domain.webhook.api.PinpointResources;

public interface SlackPayloadConvertor {
    SlackResources.Payload pinpointToSlack(PinpointResources.Payload payload);
}
