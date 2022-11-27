package com.simleetag.homework.api.domain.user.repository;

import java.util.Optional;

import com.simleetag.homework.api.domain.user.User;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.simleetag.homework.api.domain.home.QHome.home;
import static com.simleetag.homework.api.domain.home.member.QMember.member;
import static com.simleetag.homework.api.domain.user.QUser.user;

@Repository
public class UserDslRepository extends QuerydslRepositorySupport {
    public UserDslRepository() {
        super(User.class);
    }

    public Optional<User> findUserWithHomeAndMembers(Long userId) {
        return Optional.ofNullable(
                from(user)
                        .distinct()
                        .innerJoin(user.members, member).fetchJoin()
                        .innerJoin(member.home, home).fetchJoin()
                        .where(
                                user.id.eq(userId)
                                       .and(user.deletedAt.isNull())
                                       .and(member.deletedAt.isNull())
                                       .and(home.deletedAt.isNull())
                        ).fetchOne()
        );
    }
}
