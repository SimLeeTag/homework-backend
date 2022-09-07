package com.simleetag.homework.api.domain.home;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.simleetag.homework.api.common.DeletableEntity;
import com.simleetag.homework.api.domain.home.member.Member;
import com.simleetag.homework.utils.JsonMapper;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Home extends DeletableEntity {

    @OneToMany(mappedBy = "home")
    private final List<Member> members = new ArrayList<>();

    @Column
    private final boolean deleted = false;

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

    public void addCategoryId(Long categoryId) {
        final List<Long> categoryIds = getCategoryIds();
        categoryIds.add(categoryId);
        setCategoryIds(categoryIds);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getCategoryIds() {
        if (!StringUtils.hasText(textOfCategoryIds)) {
            return new ArrayList<>();
        }
        return JsonMapper.readValue(textOfCategoryIds, List.class);
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.textOfCategoryIds = JsonMapper.writeValueAsString(categoryIds);
    }
}
