package com.petverse.domain.enums;

public enum PetSpecies {
    DOG("Cachorro"),
    CAT("Gato"),
    BIRD("Pássaro"),
    RABBIT("Coelho"),
    HAMSTER("Hamster"),
    FISH("Peixe"),
    OTHER("Outro");

    private final String displayName;

    PetSpecies(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}