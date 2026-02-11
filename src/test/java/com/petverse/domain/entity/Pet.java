package com.petverse.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pets")
public class Pet extends PanacheEntity {
    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public User owner;
}
