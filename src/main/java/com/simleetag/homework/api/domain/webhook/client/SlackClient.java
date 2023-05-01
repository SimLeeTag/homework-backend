package com.simleetag.homework.api.domain.webhook.client;

import com.simleetag.homework.api.domain.webhook.SlackResources;

public interface SlackClient {
    void incomingWebhook(String path, SlackResources.Payload payload);
}
