package com.petverse.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, length = 150)
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    public  String password;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$|^$", message = "Phone must be valid")
    @Column(length = 20)
    public String phone;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    @Column(nullable = false, length = 500)
    public String bio;

    @Column(name = "profile_image_url")
    public String profileImageUrl;

    @Column(nullable = false)
    public Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,  updatable = true)
    public  LocalDateTime updatedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Pet> pets = new ArrayList<>();

    public static User findByEmail(String email) {
        return  find("email", email).firstResult();
    }

    public static List<User> listActive() {
        return list("active", true);
    }

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.owner = this;
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.owner = null;
    }
}
