package com.petverse.domain.dto;

import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class PostUpdateDTO {
    @NotBlank(message = "Post content is required")
    @Size(min = 5, max = 200, message = "Post content must be between 5 and 200 characters")
    public String content;

    @NotNull(message = "Owner ID is required")
    public User owner;

    @NotNull(message = "Pet is required")
    public List<Pet> pets = new ArrayList<>();
}
