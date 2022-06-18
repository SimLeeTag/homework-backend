package com.simleetag.homework.api.domain.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserId(Long userId);
}
