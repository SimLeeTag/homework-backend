package com.simleetag.homework.api.domain.home.repository;

import java.util.List;

import com.simleetag.homework.api.domain.home.Home;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Home, Long> {

    @EntityGraph(attributePaths = {"members"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Home> findAllWithMembersByIdIn(List<Long> homeIds);
}