package com.simleetag.homework.api.domain.user.repository;

import java.util.Optional;

import com.simleetag.homework.api.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByOauthId(String oauthId);
}
