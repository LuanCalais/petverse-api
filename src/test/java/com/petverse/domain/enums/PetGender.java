package com.petverse.domain.enums;

public enum PetGender {
    MALE("Macho"),
    FEMALE("Fêmea"),
    UNKNOWN("Desconhecido");

    private final String displayName;

    PetGender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return  this.displayName;
    }
}
