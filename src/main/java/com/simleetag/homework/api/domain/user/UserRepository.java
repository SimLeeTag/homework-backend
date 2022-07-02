package com.simleetag.homework.api.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);

    @EntityGraph(attributePaths = {"members"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findUserWithMembersById(Long userId);
}
