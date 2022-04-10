package com.simleetag.homework.common;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.PastOrPresent;

@MappedSuperclass
public abstract class DeletableEntity extends BaseEntity {

    @PastOrPresent
    protected LocalDateTime deletedAt;

    public DeletableEntity(Long id, LocalDateTime createdAt, LocalDateTime deletedAt) {
        super(id, createdAt);
        this.deletedAt = deletedAt;
    }
}
