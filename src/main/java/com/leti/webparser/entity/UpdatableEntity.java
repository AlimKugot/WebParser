package com.leti.webparser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

/**
 * Updatable base entity with fixed creation date
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class UpdatableEntity extends BaseEntity {

    /**
     * Creation date of entity
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createAt;

    /**
     * Last update of changing entity
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updateAt;



    /**
     * Fixe creation date for createAt and updateAt values
     */
    @PrePersist
    public void prePersist() {
        createAt = Instant.now();
        updateAt = Instant.now();
    }

    /**
     * Change updateAt value if entity was changed in the database
     */
    @PreUpdate
    public void preUpdate() {
        updateAt = Instant.now();
    }
}