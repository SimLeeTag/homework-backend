package com.simleetag.homework.api.domain.webhook.api;

import com.simleetag.homework.api.domain.webhook.Slack;

import java.time.LocalDateTime;

public class WebhookMaintenanceResources {
    public static class Request {
        public record Slack(
                String key,
                String path
        ) {

        }
    }

    public static class Reply {
        public record SlackChannel(
                Long id,
                LocalDateTime createdAt,
                LocalDateTime deletedAt,
                String keyCode,
                String path
        ) {
            public static SlackChannel from(Slack slack) {
                return new SlackChannel(
                        slack.getId(),
                        slack.getCreatedAt(),
                        slack.getDeletedAt(),
                        slack.getKeyCode(),
                        slack.getPath()
                );
            }
        }
    }
}
