package com.petverse.domain.service;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.dto.pet.PetUpdateDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.User;
import com.petverse.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PetService {

    @Inject
    EntityManager em;

    @Transactional
    public PetResponseDTO create(PetCreateDTO dto) {
        User owner = User.findById(dto.ownerId);

        if (owner == null || !owner.active) {
            throw new ResourceNotFoundException("Owner not found with id: " + dto.ownerId);
        }

        Pet pet = new Pet();
        pet.name = dto.name;
        pet.owner = owner;
        pet.species = dto.species;
        pet.birthDate = dto.birthDate;
        pet.breed = dto.breed;
        pet.gender = dto.gender;
        pet.size = dto.size;
        pet.weight = dto.weight;
        pet.bio = dto.bio;
        pet.profileImageUrl = dto.profileImageUrl;
        pet.microchipNumber = dto.microchipNumber;
        pet.active = true;

        pet.persist();
        em.flush();

        return new PetResponseDTO(pet);
    }

    @Transactional
    public PetResponseDTO update(Long id, PetUpdateDTO dto) {
        Pet pet = Pet.findById(id);
        if (pet == null || !pet.active) {
            throw new ResourceNotFoundException("Pet not found with id: " + id);
        }
        if (dto.name != null) {
            pet.name = dto.name;
        }
        if (dto.species != null) {
            pet.species = dto.species;
        }
        if (dto.breed != null) {
            pet.breed = dto.breed;
        }
        if (dto.gender != null) {
            pet.gender = dto.gender;
        }
        if (dto.size != null) {
            pet.size = dto.size;
        }
        if (dto.birthDate != null) {
            pet.birthDate = dto.birthDate;
        }
        if (dto.weight != null) {
            pet.weight = dto.weight;
        }
        if (dto.bio != null) {
            pet.bio = dto.bio;
        }
        if (dto.profileImageUrl != null) {
            pet.profileImageUrl = dto.profileImageUrl;
        }
        if (dto.microchipNumber != null) {
            pet.microchipNumber = dto.microchipNumber;
        }

        return new PetResponseDTO(pet);
    }

    public List<PetResponseDTO> listAll() {
        return Pet.listActive()
                .stream()
                .map(PetResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PetResponseDTO findById(Long id) {
        Pet pet = Pet.findById(id);
        if (pet == null || !pet.active) {
            throw new ResourceNotFoundException("Pet not found with id: " + id);
        }
        return new PetResponseDTO(pet);
    }

}
