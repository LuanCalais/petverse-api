package com.petverse.domain.enums;

public enum PetSize {
    SMALL("Pequeno"),
    MEDIUM("Médio"),
    LARGE("Grande"),
    EXTRA_LARGE("Extra Grande");

    private final String displayName;

    PetSize(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return  this.displayName;
    }
}
