package com.petverse.domain.dto;

import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSize;
import com.petverse.domain.enums.PetSpecies;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PetCreateDTO {

    @NotBlank(message = "Pet name is required")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    public String name;

    @NotBlank(message = "Species is required")
    public PetSpecies species;

    @Size(max = 50, message = "Breed must not exceed 50 characters")
    public String breed;

    @NotNull(message = "Gender is required")
    public PetGender gender;

    public PetSize size;

    @Past(message = "Birth date must be in the past")
    public LocalDate birthDate;

    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
    @DecimalMax(value = "200.0", message = "Weight must not exceed 200 kg")
    public Double weight;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    public String bio;

    public String profileImageUrl;

    public String microchipNumber;

    @NotNull(message = "Owner ID is required")
    public Long ownerId;
}
