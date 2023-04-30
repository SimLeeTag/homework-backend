package com.simleetag.homework.api.domain.webhook;

import com.simleetag.homework.api.common.DeletableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {@Index(name = "IDX_KEY_CODE", columnList = "keyCode", unique = true)})
public class Slack extends DeletableEntity {

    @Column
    private String keyCode;

    @Column(length = 1000)
    private String path;

    public void expire() {
        this.deletedAt = LocalDateTime.now();
    }
}
