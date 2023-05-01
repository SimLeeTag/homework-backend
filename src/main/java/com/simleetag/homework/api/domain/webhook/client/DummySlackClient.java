package com.simleetag.homework.api.domain.webhook.client;

import com.simleetag.homework.api.domain.webhook.SlackResources;
import com.simleetag.homework.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummySlackClient implements SlackClient {

    @Override
    public void incomingWebhook(String path, SlackResources.Payload payload) {
        log.info("send webhook payload is {}", JsonMapper.writeValueAsString(payload));
    }
}
