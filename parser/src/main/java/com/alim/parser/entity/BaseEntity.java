package com.alim.parser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

/**
 * Holds logic to create ID for entities
 */
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique foreign-key to create entity
     */
    @Id
    @Column(name = "id", length = 36, nullable = false, unique = true, updatable = false)
    private String id = UUID.randomUUID().toString();
}