package com.alim.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.Instant;

/**
 * Base entity with fixed creation date
 */
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public abstract class CreatedEntity extends BaseEntity {

    /**
     * Creation date
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Fixed creation date when save entity into database
     */
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}