package com.petverse.domain.dto.pet;

import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSize;
import com.petverse.domain.enums.PetSpecies;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class PetUpdateDTO {

    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    public String name;

    public PetSpecies species;

    @Size(max = 50, message = "Breed must not exceed 50 characters")
    public String breed;

    public PetGender gender;

    public PetSize size;

    @Past(message = "Birth date must be in the past")
    public java.time.LocalDate birthDate;

    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
    @DecimalMax(value = "200.0", message = "Weight must not exceed 200 kg")
    public Double weight;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    public String bio;

    public String profileImageUrl;

    public String microchipNumber;
}