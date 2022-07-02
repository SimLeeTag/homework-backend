package com.simleetag.homework.api.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserId(Long userId);

    Optional<Member> findByHomeIdAndUserId(Long homeId, Long userId);
}
