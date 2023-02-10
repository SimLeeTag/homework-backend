package com.simleetag.homework.api.domain.home.member.repository;

import java.util.List;
import java.util.Optional;

import com.simleetag.homework.api.domain.home.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByHomeIdAndUserIdAndDeletedAtIsNull(Long homeId, Long userId);

    List<Member> findAllByHomeId(Long homeId);
}
