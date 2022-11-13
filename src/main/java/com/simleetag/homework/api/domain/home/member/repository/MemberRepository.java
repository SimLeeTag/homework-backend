package com.simleetag.homework.api.domain.home.member.repository;

import java.util.List;

import com.simleetag.homework.api.domain.home.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByUserId(Long userId);

}
