package com.simleetag.homework.api.domain.webhook.client;

import com.simleetag.homework.api.config.AppEnvironment;
import com.simleetag.homework.api.domain.webhook.SlackResources;
import com.simleetag.homework.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Slf4j
public record StableSlackClient(
        AppEnvironment.Messenger.Slack slack
) implements SlackClient {

    @Override
    public void incomingWebhook(String path, SlackResources.Payload payload) {
        final String slackMessage = JsonMapper.writeValueAsString(payload);
        log.debug("send to slack via incoming webhook\npayload={}", slackMessage);
        WebClient.create()
                .post()
                .uri(path)
                .header(CONTENT_TYPE, "application/json; charset=utf-8")
                .bodyValue(slackMessage)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
