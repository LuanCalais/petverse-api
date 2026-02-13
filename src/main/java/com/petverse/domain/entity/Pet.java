package com.petverse.domain.entity;


import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSize;
import com.petverse.domain.enums.PetSpecies;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet extends PanacheEntity {

    @NotBlank(message = "Pet name is required")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    @Column(nullable = false, length = 50)
    public String name;

    @NotNull(message = "Species is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    public PetSpecies species;

    @Size(max = 50, message = "Breed must not exceed 50 characters")
    @Column(length = 50)
    public String breed;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    public PetGender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    public PetSize size;

    @Past(message = "Birth date must be in the past")
    @Column(name = "birth_date")
    public LocalDate birthDate;

    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
    @DecimalMax(value = "200.0", message = "Weight must not exceed 200 kg")
    public Double weight;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    @Column(length = 500)
    public String bio;

    @Column(name = "profile_image_url")
    public String profileImageUrl;

    @Column(name = "microchip_number", unique = true, length = 50)
    public String microchipNumber;

    @Column(nullable = false)
    public Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public User owner;

    public static List<Pet> findByOwner(User owner) {
        return list("owner", owner);
    }

    public static Pet findByMicrochip(String microchip) {
        return find("microchipNumber", microchip).firstResult();
    }

    public static List<Pet> listActive() {
        return list("active", true);
    }

    public Integer getAgeInYears() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
