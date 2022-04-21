package com.simleetag.homework.repository;

import java.util.Optional;

import com.simleetag.homework.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(Long oauthId);
}
