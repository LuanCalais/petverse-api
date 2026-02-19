package com.petverse.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {

    @NotBlank(message = "Post content is required")
    @Size(min = 5, max = 200, message = "Post content must be between 5 and 200 characters")
    @Column(nullable = false, length = 200)
    public String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public User owner;

    @NotNull(message = "Pet is required")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_pets",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    public List<Pet> pets = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    public LocalDateTime updatedAt;
}
