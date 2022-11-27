package com.simleetag.homework.api.domain.home.repository;

import java.util.Optional;

import com.simleetag.homework.api.domain.home.Home;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.simleetag.homework.api.domain.home.QHome.home;
import static com.simleetag.homework.api.domain.home.member.QMember.member;

@Repository
public class HomeDslRepository extends QuerydslRepositorySupport {
    public HomeDslRepository() {
        super(Home.class);
    }

    public Optional<Home> findHomeWithMembers(Long homeId) {
        final Home homeWithMembers = from(home)
                .innerJoin(home.members, member).fetchJoin()
                .where(
                        home.id.eq(homeId)
                               .and(home.deletedAt.isNull())
                               .and(member.deletedAt.isNull())
                ).fetchOne();

        return Optional.ofNullable(homeWithMembers);

    }
}
