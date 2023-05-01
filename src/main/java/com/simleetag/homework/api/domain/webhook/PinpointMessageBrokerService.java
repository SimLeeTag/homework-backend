package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.domain.webhook.api.PinpointResources;
import com.simleetag.homework.api.domain.webhook.client.SlackClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinpointMessageBrokerService {
    private final SlackFinder slackFinder;
    private final SlackClient slackClient;

    public void toSlack(PinpointResources.Payload request) {
        final Slack slack = slackFinder.findOneByKey("PINPOINT");
        var slackPayload = new DefaultStyleSlackPayloadConvertor(request).pinpointToSlack(request);
        slackClient.incomingWebhook(slack.getPath(), slackPayload);
    }
}
