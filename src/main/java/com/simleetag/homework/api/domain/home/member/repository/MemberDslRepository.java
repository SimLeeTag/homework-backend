package com.simleetag.homework.api.domain.home.member.repository;

import java.util.Optional;

import com.simleetag.homework.api.domain.home.member.Member;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.simleetag.homework.api.domain.home.QHome.home;
import static com.simleetag.homework.api.domain.home.member.QMember.member;

@Repository
public class MemberDslRepository extends QuerydslRepositorySupport {

    public MemberDslRepository() {
        super(Member.class);
    }

    public Optional<Member> findMemberByIdAndHomeId(Long memberId, Long homeId) {
        final Member foundMember = from(member)
                .innerJoin(home.members, member).fetchJoin()
                .where(
                        member.home.id.eq(homeId)
                                      .and(member.id.eq(memberId))
                                      .and(member.deletedAt.isNull())
                                      .and(home.deletedAt.isNull())
                ).fetchOne();

        return Optional.ofNullable(foundMember);

    }
}
