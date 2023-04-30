package com.simleetag.homework.api.domain.webhook.client;

import com.simleetag.homework.api.config.AppEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SlackClientConfiguration {
    private final AppEnvironment appEnvironment;

    @Bean
    public SlackClient slackClient() {
        var env = appEnvironment.messenger.slack;
        return env.useDummy ? new DummySlackClient() : new StableSlackClient(env);
    }
}
