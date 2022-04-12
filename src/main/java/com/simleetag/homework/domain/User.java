package com.simleetag.homework.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends DeletableEntity {

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String profileImage;

    @Builder
    public User(Long id, String userName, String profileImage, LocalDateTime createdAt, LocalDateTime deletedAt) {
        super(id, createdAt, deletedAt);
        this.userName = userName;
        this.profileImage = profileImage;
    }
}
