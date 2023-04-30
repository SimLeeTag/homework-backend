package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.domain.webhook.api.WebhookMaintenanceResources;
import com.simleetag.homework.api.domain.webhook.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SlackService {
    private final SlackFinder slackFinder;
    private final SlackRepository slackRepository;

    public void add(WebhookMaintenanceResources.Request.Slack request) {
        Slack slack = new Slack(request.key(), request.path());
        slackRepository.save(slack);
    }

    public void expireAll(List<Long> ids) {
        var slacks = slackFinder.findAllById(ids);
        slacks.forEach(Slack::expire);
    }
}
