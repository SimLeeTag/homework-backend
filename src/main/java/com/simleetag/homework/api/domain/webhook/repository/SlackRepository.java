package com.simleetag.homework.api.domain.webhook.repository;

import com.simleetag.homework.api.domain.webhook.Slack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackRepository extends JpaRepository<Slack, Long> {
    Slack findByKeyCode(String keyCode);
}
