package com.simleetag.homework.domain;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.PastOrPresent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class DeletableEntity extends BaseEntity {

    @PastOrPresent
    protected LocalDateTime deletedAt;

    protected DeletableEntity(Long id, LocalDateTime createdAt, LocalDateTime deletedAt) {
        super(id, createdAt);
        this.deletedAt = deletedAt;
    }
}

