package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.domain.webhook.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SlackFinder {
    private static final IllegalArgumentException NOT_FOUND_EXCEPTION =
            new IllegalArgumentException("해당 슬랙 정보가 존재하지 않습니다.");

    private final SlackRepository slackRepository;

    public Slack findById(Long id) {
        return slackRepository.findById(id).orElseThrow(() -> NOT_FOUND_EXCEPTION);
    }

    public Slack findOneByKey(String key) {
        return slackRepository.findByKeyCode(key);
    }

    public List<Slack> findAllById(List<Long> ids) {
        return slackRepository.findAllById(ids);
    }
}
