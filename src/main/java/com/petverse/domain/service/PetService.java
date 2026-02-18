package com.petverse.domain.service;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
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

        if (owner == null) {
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
