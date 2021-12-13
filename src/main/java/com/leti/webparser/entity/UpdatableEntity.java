package com.leti.webparser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class UpdatableEntity extends BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updateAt;

    @PrePersist
    public void prePersist() {
        createAt = Instant.now();
        updateAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = Instant.now();
    }
}