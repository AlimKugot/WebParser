package com.leti.webparser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.Instant;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public abstract class CreatedEntity extends BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createAt;

    @PrePersist
    public void prePersist() {
        createAt = Instant.now();
    }
}