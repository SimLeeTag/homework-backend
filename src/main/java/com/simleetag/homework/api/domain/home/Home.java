package com.simleetag.homework.api.domain.home;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.api.dto.HomeModifyRequest;
import com.simleetag.homework.api.domain.home.member.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@Entity
public class Home extends DeletableEntity {

    @OneToMany(mappedBy = "home")
    private final List<Member> members = new ArrayList<>();

    @Column
    private String homeName;

    @Column
    private String textOfCategoryIds;

    public Home(String homeName) {
        this.homeName = homeName;
    }

    public void addBy(Member member) {
        this.members.add(member);
        if (member.getHome() != this) {
            member.setBy(this);
        }
    }

    public void modify(HomeModifyRequest request) {
        this.homeName = request.homeName();
    }

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }

    public List<Long> getTextOfCategoryIds() {
        if (StringUtils.isBlank(textOfCategoryIds)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                Arrays.stream(textOfCategoryIds.split(","))
                      .map(Long::valueOf)
                      .toList()
        );
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.textOfCategoryIds = StringUtils.join(categoryIds, ",");
    }

    public void addCategoryId(Long id) {
        final List<Long> categoryIds = getTextOfCategoryIds();
        categoryIds.add(id);
        setCategoryIds(categoryIds);
    }

    public void kickOutAll(List<Member> members) {
        members.forEach(Member::expire);
    }
}
