package com.simleetag.homework.api.domain.home;

import com.simleetag.homework.api.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<User, Long> {

}
