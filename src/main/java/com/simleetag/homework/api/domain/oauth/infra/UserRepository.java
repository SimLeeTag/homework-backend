package com.simleetag.homework.api.domain.oauth.infra;

import java.util.Optional;

import com.simleetag.homework.api.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);
}
