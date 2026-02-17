package com.petverse.domain.dto;

import com.petverse.domain.entity.Pet;
import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSize;
import com.petverse.domain.enums.PetSpecies;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PetResponseDTO {

    public Long id;
    public String name;
    public PetSpecies species;
    public String breed;
    public PetGender gender;
    public PetSize size;
    public LocalDate birthDate;
    public Double weight;
    public String bio;
    public String profileImageUrl;
    public String microchipNumber;

    public Boolean active;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public Long ownerId;

    public PetResponseDTO() {
    }

    public PetResponseDTO(Pet pet) {
        this.id = pet.id;
        this.name = pet.name;
        this.species = pet.species;
        this.breed = pet.breed;
        this.gender = pet.gender;
        this.size = pet.size;
        this.birthDate = pet.birthDate;
        this.weight = pet.weight;
        this.bio = pet.bio;
        this.profileImageUrl = pet.profileImageUrl;
        this.microchipNumber = pet.microchipNumber;
        this.active = pet.active;
        this.createdAt = pet.createdAt;
        this.updatedAt = pet.updatedAt;
        this.ownerId = pet.owner != null ? pet.owner.id : null;
    }
}
