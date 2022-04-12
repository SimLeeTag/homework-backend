package com.simleetag.homework.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    @Column(nullable = false)
    protected LocalDateTime createdAt;

    protected BaseEntity(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
